package az.edu.itbrains.ecommerce.helpers;

import az.edu.itbrains.ecommerce.services.PromotionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Hər saat başlayan promosyonların statusunu yoxlayır.
 * Bitmüş ACTIVE promosyonları EXPIRED edir.
 */
@Component
@RequiredArgsConstructor
public class PromotionExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(PromotionExpiryScheduler.class);

    private final PromotionService promotionService;

    // Hər saat — fixedRate ilə 60 dəqiqədə bir işləyir
    @Scheduled(fixedRate = 3_600_000)
    public void expirePromotions() {
        log.debug("Running promotion expiry scheduler...");
        promotionService.expireOutdatedPromotions();
    }
}
