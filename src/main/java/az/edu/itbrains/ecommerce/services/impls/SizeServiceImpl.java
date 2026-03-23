package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.size.SizeCreateDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeUpdateDto;
import az.edu.itbrains.ecommerce.models.Size;
import az.edu.itbrains.ecommerce.repositories.SizeRepository;
import az.edu.itbrains.ecommerce.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<SizeDto> getAllSizes() {
        List<Size> sizes = sizeRepository.findAll();
        if (!sizes.isEmpty()) {
            return sizes.stream().map(size -> modelMapper.map(size, SizeDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public boolean saveSize(SizeCreateDto sizeCreateDto) {
        try {
            Size size = new Size();
            size.setSize(sizeCreateDto.getSize());
            sizeRepository.save(size);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public SizeUpdateDto findUpdatedSize(Long id) {
        Objects.requireNonNull(id, "Size id must not be null");
        Size size = sizeRepository.findById(id).orElseThrow();
        SizeUpdateDto sizeUpdateDto = modelMapper.map(size, SizeUpdateDto.class);
        return sizeUpdateDto;
    }

    @Override
    public boolean updateSize(Long id, SizeUpdateDto sizeUpdateDto) {
        Objects.requireNonNull(id, "Size id must not be null");
        Size size = sizeRepository.findById(id).orElseThrow();
        size.setSize(sizeUpdateDto.getSize());
        sizeRepository.save(size);
        return true;
    }

    @Override
    public Size getSizeById(Long sizeId) {
        Objects.requireNonNull(sizeId, "Size id must not be null");
        return sizeRepository.findById(sizeId).orElseThrow();
    }
}
