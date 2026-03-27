package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.size.SizeCreateDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeUpdateDto;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Size;
import az.edu.itbrains.ecommerce.repositories.SizeRepository;
import az.edu.itbrains.ecommerce.services.SizeService;
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
public class SizeServiceImpl implements SizeService {

    private static final Logger log = LoggerFactory.getLogger(SizeServiceImpl.class);

    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<SizeDto> getAllSizes() {
        return sizeRepository.findAll()
                .stream()
                .map(size -> modelMapper.map(size, SizeDto.class))
                .toList();
    }

    @Override
    @Transactional
    public boolean saveSize(SizeCreateDto sizeCreateDto) {
        try {
            Size size = new Size();
            size.setSize(sizeCreateDto.getSize());
            sizeRepository.save(size);
            return true;
        } catch (Exception e) {
            log.error("Failed to save size: {}", sizeCreateDto.getSize(), e);
            throw new ServiceException("Failed to save size: " + sizeCreateDto.getSize(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SizeUpdateDto findUpdatedSize(Long id) {
        Objects.requireNonNull(id, "Size id must not be null");
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Size"));
        return modelMapper.map(size, SizeUpdateDto.class);
    }

    @Override
    @Transactional
    public boolean updateSize(Long id, SizeUpdateDto sizeUpdateDto) {
        Objects.requireNonNull(id, "Size id must not be null");
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Size"));
        size.setSize(sizeUpdateDto.getSize());
        sizeRepository.save(size);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Size getSizeById(Long sizeId) {
        Objects.requireNonNull(sizeId, "Size id must not be null");
        return sizeRepository.findById(sizeId)
                .orElseThrow(() -> new ResourceNotFoundException(sizeId, "Size"));
    }

    @Override
    @Transactional
    public boolean deleteSize(Long id) {
        Objects.requireNonNull(id, "Size id must not be null");
        sizeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Size"));
        try {
            sizeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete size with id: {}", id, e);
            throw new ServiceException("Failed to delete size with id: " + id, e);
        }
    }
}
