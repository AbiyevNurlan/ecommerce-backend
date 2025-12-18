package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.color.ColorProductCreateDto;
import az.edu.itbrains.ecommerce.dtos.colorSize.ColorSizeCreateDto;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.ColorSize;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.Size;
import az.edu.itbrains.ecommerce.repositories.ColorSizeRepository;
import az.edu.itbrains.ecommerce.services.ColorService;
import az.edu.itbrains.ecommerce.services.ColorSizeService;
import az.edu.itbrains.ecommerce.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ColorSizeServiceImpl implements ColorSizeService {
    private final ColorSizeRepository colorSizeRepository;
    private final ModelMapper modelMapper;
    private final ColorService colorService;
    private final SizeService sizeService;

    @Override
    public boolean createColorSize(List<ColorSizeCreateDto> colorSizes, Product product) {

       try {
           for (ColorSizeCreateDto colorSizeCreateDto : colorSizes) {
               for (ColorProductCreateDto colorProductCreateDto : colorSizeCreateDto.getProductSizes()) {
                   Color color = colorService.getColorById(colorSizeCreateDto.getColorId());
                   Size size = sizeService.getSizeById(colorProductCreateDto.getSizeId());
                   ColorSize colorSize = new ColorSize();
                   colorSize.setProduct(product);
                   colorSize.setColor(color);
                   colorSize.setQuantity(colorProductCreateDto.getQuantity());
                   colorSize.setSize(size);
                   colorSizeRepository.save(colorSize);
               }
           }
           return true;
       }catch (Exception e){
           System.out.println(e.getMessage());
           return false;
       }
    }
}
