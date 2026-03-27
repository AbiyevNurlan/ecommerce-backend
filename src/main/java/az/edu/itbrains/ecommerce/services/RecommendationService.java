package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.models.Product;

import java.util.List;

public interface RecommendationService {

    /**
     * Item-based collaborative filtering.
     *
     * Analyses co-purchase patterns across all completed orders using the query:
     *   "Which other products appear most often in the same order as product X?"
     *
     * When purchase history is insufficient (new product / cold start),
     * falls back to products in the same category ranked by recency.
     *
     * @param productId the product whose detail page is being viewed
     * @return up to 4 recommended products, ordered by co-purchase frequency
     */
    List<Product> getFrequentlyBoughtTogether(Long productId);
}
