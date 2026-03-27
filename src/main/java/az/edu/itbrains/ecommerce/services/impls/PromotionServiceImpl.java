package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.seller.PromotionCreateDto;
import az.edu.itbrains.ecommerce.dtos.seller.PromotionDto;
import az.edu.itbrains.ecommerce.enums.PromotionStatus;
import az.edu.itbrains.ecommerce.enums.PromotionType;
import az.edu.itbrains.ecommerce.enums.TransactionType;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.Promotion;
import az.edu.itbrains.ecommerce.models.Seller;
import az.edu.itbrains.ecommerce.models.SellerTransaction;
import az.edu.itbrains.ecommerce.repositories.ProductRepository;
import az.edu.itbrains.ecommerce.repositories.PromotionRepository;
import az.edu.itbrains.ecommerce.repositories.SellerRepository;
import az.edu.itbrains.ecommerce.repositories.SellerTransactionRepository;
import az.edu.itbrains.ecommerce.services.PromotionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PromotionServiceImpl implements PromotionService {

    private static final Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

    /**
     * Qiymət cədvəli: PromotionType → (günSayı → qiymət)
     * FEATURED    : 3g=10₼  7g=20₼  30g=60₼
     * SPONSORED   : 3g=5₼   7g=10₼  30g=30₼
     * HOT_TRENDING: 3g=7₼   7g=15₼  30g=45₼
     */
    private static final Map<PromotionType, Map<Integer, BigDecimal>> PRICE_TABLE = Map.of(
            PromotionType.FEATURED,     Map.of(3, new BigDecimal("10.00"), 7, new BigDecimal("20.00"), 30, new BigDecimal("60.00")),
            PromotionType.SPONSORED,    Map.of(3, new BigDecimal("5.00"),  7, new BigDecimal("10.00"), 30, new BigDecimal("30.00")),
            PromotionType.HOT_TRENDING, Map.of(3, new BigDecimal("7.00"),  7, new BigDecimal("15.00"), 30, new BigDecimal("45.00"))
    );

    private final PromotionRepository promotionRepository;
    private final SellerRepository sellerRepository;
    private final SellerTransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PromotionDto buyPromotion(String email, PromotionCreateDto dto) {
        Seller seller = sellerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));

        if (!seller.isApproved()) {
            throw new ServiceException("Hesabınız hələ təsdiqlənməmişdir");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getProductId(), "Product"));

        // Məhsulun bu satıcıya aid olub-olmadığını yoxla
        if (product.getSeller() == null || !product.getSeller().getId().equals(seller.getId())) {
            throw new ServiceException("Bu məhsul sizə aid deyil");
        }

        // Eyni tip aktiv promosyon varmı?
        boolean alreadyActive = promotionRepository.existsByProductIdAndSellerIdAndPromotionTypeAndStatusIn(
                product.getId(), seller.getId(), dto.getPromotionType(),
                List.of(PromotionStatus.ACTIVE, PromotionStatus.PENDING));
        if (alreadyActive) {
            throw new ServiceException("Bu məhsul üçün eyni tipli aktiv promosyon artıq mövcuddur");
        }

        BigDecimal price = calculatePrice(dto.getPromotionType(), dto.getDurationDays());

        if (seller.getBalance().compareTo(price) < 0) {
            throw new ServiceException(
                    String.format("Kifayət qədər balans yoxdur. Lazım olan: %.2f ₼, mövcud: %.2f ₼",
                            price, seller.getBalance()));
        }

        // Balansdan düş
        seller.setBalance(seller.getBalance().subtract(price));
        sellerRepository.save(seller);

        // Transaksiyanı qeyd et
        SellerTransaction tx = SellerTransaction.builder()
                .seller(seller)
                .amount(price.negate())
                .transactionType(TransactionType.PROMO_DEBIT)
                .description(dto.getPromotionType() + " — " + dto.getDurationDays() + " gün")
                .build();
        transactionRepository.save(tx);

        // Promosyonu yarat
        LocalDateTime now = LocalDateTime.now();
        Promotion promotion = Promotion.builder()
                .product(product)
                .seller(seller)
                .promotionType(dto.getPromotionType())
                .startDate(now)
                .endDate(now.plusDays(dto.getDurationDays()))
                .amountPaid(price)
                .status(PromotionStatus.ACTIVE)
                .build();
        promotionRepository.save(promotion);

        log.info("Promotion bought — seller={}, type={}, productId={}, days={}, price={}",
                email, dto.getPromotionType(), dto.getProductId(), dto.getDurationDays(), price);

        return toDto(promotion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionDto> getSellerPromotions(String email) {
        Seller seller = sellerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        return promotionRepository.findBySellerIdOrderByCreatedAtDesc(seller.getId())
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public void cancelPromotion(String email, Long promotionId) {
        Seller seller = sellerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException(promotionId, "Promotion"));

        if (!promotion.getSeller().getId().equals(seller.getId())) {
            throw new ServiceException("Bu promosyon sizə aid deyil");
        }
        if (promotion.getStatus() == PromotionStatus.EXPIRED) {
            throw new ServiceException("Bitmüş promosyon ləğv edilə bilməz");
        }
        promotion.setStatus(PromotionStatus.CANCELLED);
        promotionRepository.save(promotion);
        log.info("Promotion cancelled — id={}, seller={}", promotionId, email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getFeaturedProducts() {
        List<Long> promoIds = promotionRepository.findActiveProductIdsByType(
                PromotionType.FEATURED, LocalDateTime.now());
        return productRepository.findFeaturedOrPromoted(promoIds.isEmpty() ? List.of(-1L) : promoIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getHotTrendingProducts() {
        List<Long> promoIds = promotionRepository.findActiveProductIdsByType(
                PromotionType.HOT_TRENDING, LocalDateTime.now());
        return productRepository.findHotTrendingOrPromoted(promoIds.isEmpty() ? List.of(-1L) : promoIds);
    }

    @Override
    @Transactional
    public void expireOutdatedPromotions() {
        List<Promotion> expired = promotionRepository.findByStatusAndEndDateBefore(
                PromotionStatus.ACTIVE, LocalDateTime.now());
        expired.forEach(p -> p.setStatus(PromotionStatus.EXPIRED));
        if (!expired.isEmpty()) {
            promotionRepository.saveAll(expired);
            log.info("Expired {} promotions", expired.size());
        }
    }

    @Override
    public BigDecimal calculatePrice(PromotionType type, int days) {
        Map<Integer, BigDecimal> prices = PRICE_TABLE.get(type);
        if (prices == null) {
            throw new ServiceException("Bilinməyən promosyon tipi: " + type);
        }
        BigDecimal price = prices.get(days);
        if (price == null) {
            throw new ServiceException("Bu müddət dəstəklənmir: " + days +
                    " gün. Dəstəklənən müddətlər: 3, 7, 30 gün");
        }
        return price;
    }

    // ── private ───────────────────────────────────────────────────────────────

    private PromotionDto toDto(Promotion p) {
        return new PromotionDto(
                p.getId(),
                p.getProduct().getId(),
                p.getProduct().getName(),
                p.getPromotionType(),
                p.getStartDate(),
                p.getEndDate(),
                p.getAmountPaid(),
                p.getStatus(),
                p.getCreatedAt()
        );
    }
}
