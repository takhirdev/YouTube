package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.CategoryDto;
import you_tube_own.entity.CategoryEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.CategoryRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto create(CategoryDto dto) {
        categoryRepository.findByName(dto.getName())
                .ifPresent(entity -> {
                    throw new AppBadException("Category name already exists");
                });
        CategoryEntity saved = categoryRepository.save(toEntity(dto));
        return toDto(saved);
    }


    public CategoryDto update(Integer id, CategoryDto dto) {
        CategoryEntity entity = findById(id);
        entity.setName(dto.getName());
        CategoryEntity saved = categoryRepository.save(entity);
        return toDto(saved);
    }

    public void delete(Integer id) {
        CategoryEntity entity = findById(id);
        categoryRepository.delete(entity);
    }

    public Page<CategoryDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CategoryEntity> entityPage = categoryRepository.findAllBy(pageable);
        List<CategoryDto> list = entityPage.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list,pageable,totalElements);
    }

    public CategoryEntity findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Category not found"));
    }

    private CategoryDto toDto(CategoryEntity categoryEntity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(categoryEntity.getId());
        dto.setCreatedDate(categoryEntity.getCreatedDate());
        dto.setName(categoryEntity.getName());
        return dto;
    }

    private CategoryEntity toEntity(CategoryDto dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        return entity;
    }
}
