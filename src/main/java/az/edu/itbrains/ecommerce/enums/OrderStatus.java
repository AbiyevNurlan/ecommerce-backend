package az.edu.itbrains.ecommerce.enums;

public enum OrderStatus {
    PENDING,        // Order placed but not yet processed
    CONFIRMED,      // Payment or confirmation received
    PROCESSING,     // Being prepared / packaged
    SHIPPED,        // Left the warehouse
    IN_TRANSIT,     // On the way to customer
    OUT_FOR_DELIVERY, // Carrier is delivering
    DELIVERED,      // Customer received it
    CANCELLED,      // Cancelled before fulfillment
    RETURN_REQUESTED, // Customer asked to return
    RETURNED,       // Returned and received
    FAILED          // Payment or processing failure
}
