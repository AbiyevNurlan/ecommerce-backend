package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.color.ColorCreateDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorUpdateDto;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Size;

import java.util.List;

public interface ColorService {
    List<ColorDto> getAllColors();

    boolean saveColor(ColorCreateDto colorCreateDto);

    ColorUpdateDto findUpdatedColor(Long id);

    boolean updateColor(Long id, ColorUpdateDto colorUpdateDto);

    Color getColorById(Long colorId);

}
