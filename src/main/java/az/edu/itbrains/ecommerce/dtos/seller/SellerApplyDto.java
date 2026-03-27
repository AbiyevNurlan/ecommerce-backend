package az.edu.itbrains.ecommerce.dtos.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerApplyDto {

    @NotBlank(message = "Mağaza adı boş ola bilməz")
    @Size(min = 3, max = 100, message = "Mağaza adı 3-100 simvol arasında olmalıdır")
    private String shopName;

    @Size(max = 1000, message = "Açıqlama 1000 simvoldan çox ola bilməz")
    private String shopDescription;
}
