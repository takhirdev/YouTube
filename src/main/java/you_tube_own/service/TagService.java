package you_tube_own.service;

import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.tag.TagDto;
import you_tube_own.entity.TagEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.TagRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagDto create(TagDto dto) {
        tagRepository.findByName(dto.getName())
                .ifPresent(entity -> {
                    throw new AppBadException("tag name already exists");
                });

        TagEntity saved = tagRepository.save(toEntity(dto));
        return toDto(saved);
    }

    public TagDto update(Integer id, TagDto dto) {
        TagEntity tagEntity = findById(id);
        tagEntity.setName(dto.getName());
        TagEntity updated = tagRepository.save(tagEntity);
        return toDto(updated);
    }

    public void delete(Integer id) {
        TagEntity tagEntity = findById(id);
        tagRepository.delete(tagEntity);
    }

    public List<TagDto> getAll() {
        Iterable<TagEntity> all = tagRepository.findAll();
        List<TagDto> list = new LinkedList<>();
        for (TagEntity tagEntity : all) {
            TagDto tagDto = new TagDto();
            tagDto.setId(tagEntity.getId());
            tagDto.setName(tagEntity.getName());
            tagDto.setCreatedDate(tagEntity.getCreatedDate());
            list.add(tagDto);
        }
        return list;
    }

    public Page<TagDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<TagEntity> entityPage = tagRepository.findAllBy(pageable);
        List<TagDto> list = entityPage.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public TagEntity findById(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Tag not found"));

    }

    private TagDto toDto(TagEntity entity) {
        TagDto dto = new TagDto();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setName(entity.getName());
        return dto;
    }

    private TagEntity toEntity(TagDto dto) {
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        return entity;
    }
}
