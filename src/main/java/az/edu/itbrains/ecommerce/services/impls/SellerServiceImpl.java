package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.seller.BalanceCreditDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerAdminDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerApplyDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerDashboardDto;
import az.edu.itbrains.ecommerce.enums.PromotionStatus;
import az.edu.itbrains.ecommerce.enums.TransactionType;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Role;
import az.edu.itbrains.ecommerce.models.Seller;
import az.edu.itbrains.ecommerce.models.SellerTransaction;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.RoleRepository;
import az.edu.itbrains.ecommerce.repositories.SellerRepository;
import az.edu.itbrains.ecommerce.repositories.SellerTransactionRepository;
import az.edu.itbrains.ecommerce.repositories.UserRepository;
import az.edu.itbrains.ecommerce.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class SellerServiceImpl implements SellerService {

    private static final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;
    private final SellerTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public boolean applyForSeller(String email, SellerApplyDto dto) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("İstifadəçi tapılmadı: " + email);
        }
        if (sellerRepository.existsByUserId(user.getId())) {
            throw new ServiceException("Bu istifadəçi artıq satıcı müraciəti etmişdir");
        }
        if (sellerRepository.existsByShopName(dto.getShopName())) {
            throw new ServiceException("Bu mağaza adı artıq mövcuddur: " + dto.getShopName());
        }

        Seller seller = Seller.builder()
                .user(user)
                .shopName(dto.getShopName())
                .shopDescription(dto.getShopDescription())
                .build();
        sellerRepository.save(seller);
        log.info("Seller application submitted — user={}, shop={}", email, dto.getShopName());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Seller findByEmail(String email) {
        return sellerRepository.findByUserEmail(email).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Seller getById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Seller"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerAdminDto> getAllSellers() {
        return sellerRepository.findAll().stream()
                .map(s -> new SellerAdminDto(
                        s.getId(),
                        s.getUser().getId(),
                        s.getUser().getEmail(),
                        s.getUser().getName() + " " + s.getUser().getSurname(),
                        s.getShopName(),
                        s.getShopDescription(),
                        s.getBalance(),
                        s.getCommissionRate(),
                        s.isApproved(),
                        s.getProducts().size(),
                        s.getCreatedAt()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void approveSeller(Long sellerId) {
        Seller seller = getById(sellerId);
        seller.setApproved(true);

        // ROLE_SELLER rolunu istifadəçiyə əlavə et
        Role sellerRole = roleRepository.findByName("ROLE_SELLER");
        if (sellerRole != null && !seller.getUser().getRoles().contains(sellerRole)) {
            seller.getUser().getRoles().add(sellerRole);
            userRepository.save(seller.getUser());
        }
        sellerRepository.save(seller);
        log.info("Seller approved — id={}, shop={}", sellerId, seller.getShopName());
    }

    @Override
    @Transactional
    public void rejectSeller(Long sellerId) {
        Seller seller = getById(sellerId);
        seller.setApproved(false);

        // ROLE_SELLER rolunu istifadəçidən çıxart
        Role sellerRole = roleRepository.findByName("ROLE_SELLER");
        if (sellerRole != null) {
            seller.getUser().getRoles().remove(sellerRole);
            userRepository.save(seller.getUser());
        }
        sellerRepository.save(seller);
        log.info("Seller rejected — id={}, shop={}", sellerId, seller.getShopName());
    }

    @Override
    @Transactional(readOnly = true)
    public SellerDashboardDto getDashboardStats(String email) {
        Seller seller = sellerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));

        long totalOrders = orderItemRepository
                .countDistinctOrdersBySellerProducts(seller.getProducts().stream().map(p -> p.getId()).toList());

        BigDecimal totalRevenue = orderItemRepository
                .sumRevenueByProductIds(seller.getProducts().stream().map(p -> p.getId()).toList());

        long activePromos = seller.getPromotions().stream()
                .filter(p -> p.getStatus() == PromotionStatus.ACTIVE)
                .count();

        return new SellerDashboardDto(
                seller.getShopName(),
                seller.getBalance(),
                seller.getCommissionRate(),
                seller.isApproved(),
                seller.getProducts().size(),
                (int) activePromos,
                totalOrders,
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO
        );
    }

    @Override
    @Transactional
    public void creditBalance(BalanceCreditDto dto) {
        Seller seller = getById(dto.getSellerId());
        seller.setBalance(seller.getBalance().add(dto.getAmount()));
        sellerRepository.save(seller);

        SellerTransaction tx = SellerTransaction.builder()
                .seller(seller)
                .amount(dto.getAmount())
                .transactionType(TransactionType.CREDIT)
                .description(dto.getDescription() != null ? dto.getDescription() : "Admin balans yükləməsi")
                .build();
        transactionRepository.save(tx);
        log.info("Balance credited — sellerId={}, amount={}", dto.getSellerId(), dto.getAmount());
    }
}
