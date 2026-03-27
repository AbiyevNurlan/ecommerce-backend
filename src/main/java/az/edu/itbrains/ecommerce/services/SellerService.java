package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.seller.BalanceCreditDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerAdminDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerApplyDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerDashboardDto;
import az.edu.itbrains.ecommerce.models.Seller;

import java.util.List;

public interface SellerService {

    /** İstifadəçi satıcılıq üçün müraciət edir */
    boolean applyForSeller(String email, SellerApplyDto dto);

    /** Email ilə satıcı tapılır (yoxdursa null) */
    Seller findByEmail(String email);

    /** Id ilə satıcı tapılır */
    Seller getById(Long id);

    /** Admin — bütün satıcıların siyahısı */
    List<SellerAdminDto> getAllSellers();

    /** Admin — satıcı müraciətini təsdiq edir */
    void approveSeller(Long sellerId);

    /** Admin — satıcı müraciətini rədd edir / bloklanır */
    void rejectSeller(Long sellerId);

    /** Satıcının dashboard statistikası */
    SellerDashboardDto getDashboardStats(String email);

    /** Admin — satıcı balansına kredit əlavə edir */
    void creditBalance(BalanceCreditDto dto);
}
