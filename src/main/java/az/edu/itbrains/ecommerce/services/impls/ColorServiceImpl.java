package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.color.ColorCreateDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorUpdateDto;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Size;
import az.edu.itbrains.ecommerce.repositories.ColorRepository;
import az.edu.itbrains.ecommerce.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<ColorDto> getAllColors() {
        List<Color> colors = colorRepository.findAll();
        if (!colors.isEmpty()) {
            return colors.stream().map(color -> modelMapper.map(color, ColorDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public boolean saveColor(ColorCreateDto colorCreateDto) {
        try {
            Color color = new Color();
            color.setName(colorCreateDto.getName());
            colorRepository.save(color);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public ColorUpdateDto findUpdatedColor(Long id) {
        Color color = colorRepository.findById(id).orElseThrow();
        ColorUpdateDto colorUpdateDto = modelMapper.map(color, ColorUpdateDto.class);
        return colorUpdateDto;
    }

    @Override
    public boolean updateColor(Long id, ColorUpdateDto colorUpdateDto) {
        Color color = colorRepository.findById(id).orElseThrow();
        color.setName(colorUpdateDto.getName());
        colorRepository.save(color);
        return true;
    }

    @Override
    public Color getColorById(Long colorId) {
        return colorRepository.findById(colorId).orElseThrow();
    }


}
