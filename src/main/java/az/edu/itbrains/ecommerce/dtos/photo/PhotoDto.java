package az.edu.itbrains.ecommerce.dtos.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto {
    private Long id;
    private String url;
    private boolean selected;
}
