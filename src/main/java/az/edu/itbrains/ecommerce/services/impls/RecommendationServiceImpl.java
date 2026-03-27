package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.ProductRepository;
import az.edu.itbrains.ecommerce.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    /**
     * Item-based Collaborative Filtering algorithm:
     *
     *   Step 1 — Find all orders that contain the target product.
     *   Step 2 — Collect every OTHER product that appears in those same orders.
     *   Step 3 — Rank by co-occurrence frequency (highest frequency = most relevant).
     *   Step 4 — Return top 4.
     *
     * Cold-start fallback: when no co-purchase data exists yet, return the 4
     * most recent products in the same category (content-based fallback).
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getFrequentlyBoughtTogether(Long productId) {
        List<Long> coIds = orderItemRepository.findFrequentlyBoughtTogetherIds(productId);

        if (!coIds.isEmpty()) {
            return productRepository.findAllById(coIds);
        }

        // Cold-start fallback: same category, most recent 4
        return productRepository.findById(productId)
                .map(p -> productRepository.findTop4ByCategoryIdAndIdNot(
                        p.getCategory().getId(), productId))
                .orElse(List.of());
    }
}
