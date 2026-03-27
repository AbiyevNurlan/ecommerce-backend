package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.color.ColorCreateDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorUpdateDto;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.repositories.ColorRepository;
import az.edu.itbrains.ecommerce.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private static final Logger log = LoggerFactory.getLogger(ColorServiceImpl.class);

    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<ColorDto> getAllColors() {
        return colorRepository.findAll()
                .stream()
                .map(color -> modelMapper.map(color, ColorDto.class))
                .toList();
    }

    @Override
    @Transactional
    public boolean saveColor(ColorCreateDto colorCreateDto) {
        try {
            Color color = new Color();
            color.setName(colorCreateDto.getName());
            colorRepository.save(color);
            return true;
        } catch (Exception e) {
            log.error("Failed to save color: {}", colorCreateDto.getName(), e);
            throw new ServiceException("Failed to save color: " + colorCreateDto.getName(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ColorUpdateDto findUpdatedColor(Long id) {
        Objects.requireNonNull(id, "Color id must not be null");
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Color"));
        return modelMapper.map(color, ColorUpdateDto.class);
    }

    @Override
    @Transactional
    public boolean updateColor(Long id, ColorUpdateDto colorUpdateDto) {
        Objects.requireNonNull(id, "Color id must not be null");
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Color"));
        color.setName(colorUpdateDto.getName());
        colorRepository.save(color);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Color getColorById(Long colorId) {
        Objects.requireNonNull(colorId, "Color id must not be null");
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new ResourceNotFoundException(colorId, "Color"));
    }

    @Override
    @Transactional
    public boolean deleteColor(Long id) {
        Objects.requireNonNull(id, "Color id must not be null");
        colorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Color"));
        try {
            colorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete color with id: {}", id, e);
            throw new ServiceException("Failed to delete color with id: " + id, e);
        }
    }
}
