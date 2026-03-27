package az.edu.itbrains.ecommerce.enums;

public enum PromotionStatus {
    PENDING,    // Ödəniş edilib, aktivasiya gözlənilir (və ya avtomatik start_date gəlməyib)
    ACTIVE,     // Hal-hazırda aktiv
    EXPIRED,    // end_date keçdi — scheduler tərəfindən yenilənir
    CANCELLED   // Satıcı ləğv etdi (aktiv olmamış)
}
