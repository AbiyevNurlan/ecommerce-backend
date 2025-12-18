package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.colorSize.ColorSizeCreateDto;
import az.edu.itbrains.ecommerce.models.Product;

import java.util.List;

public interface ColorSizeService {
    boolean createColorSize(List<ColorSizeCreateDto> colorSizes, Product product);
}
