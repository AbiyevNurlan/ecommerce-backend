package az.edu.itbrains.ecommerce.enums;

public enum ProductStatus {
    DRAFT,          // Satıcı hələ tamamlamamış
    PENDING_REVIEW, // Admin təsdiqini gözləyir
    ACTIVE,         // Satışda
    REJECTED,       // Admin rədd etdi
    SUSPENDED       // Admin dayandırdı
}
