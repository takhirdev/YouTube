package you_tube_own.service;

import com.sun.jdi.event.StepEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import you_tube_own.dto.tag.TagDto;
import you_tube_own.dto.tag.VideoTagCreateDto;
import you_tube_own.dto.tag.VideoTagDto;
import you_tube_own.entity.VideoTagEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.VideoTagRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoTagService {
    private final VideoTagRepository videoTagRepository;

    @Transactional
    public void merge(String videoId, List<String> newList) {
        Objects.requireNonNull(newList, "New types list must not be null");
        List<String> oldList = videoTagRepository.findAllTagsIdByVideoId(videoId);
        if (oldList.isEmpty()) {
            bulkInsert(videoId, newList);
            return;
        }

        Set<String> toDelete = new HashSet<>(oldList);
        newList.forEach(toDelete::remove);

        Set<String> toAdd = new HashSet<>(newList);
        oldList.forEach(toAdd::remove);

        if (!toAdd.isEmpty()) {
            bulkInsert(videoId, new ArrayList<>(toAdd));
        }

        if (!toDelete.isEmpty()) {
            videoTagRepository.deleteAllByVideoIdAndTagList(videoId, new ArrayList<>(toDelete));
        }
    }

    private void bulkInsert(String videoId, List<String> tagsId) {
        List<VideoTagEntity> entities = tagsId.stream()
                .map(tagId -> {
                    VideoTagEntity entity = new VideoTagEntity();
                    entity.setVideoId(videoId);
                    entity.setTagId(tagId);
                    return entity;
                })
                .collect(Collectors.toList());

        videoTagRepository.saveAll(entities);
    }

    public List<VideoTagDto> getTagByVideoId(String videoId) {
        return videoTagRepository.findAllByVideoId(videoId)
                .stream()
                .map(entity -> {
                    VideoTagDto dto = new VideoTagDto();
                    dto.setId(entity.getId());
                    dto.setVideoId(entity.getVideoId());
                    dto.setCreatedDate(entity.getTagCreatedDate());
                    // create tag
                    TagDto tag = new TagDto();
                    tag.setId(entity.getTagId());
                    tag.setName(entity.getTagName());
                    dto.setTag(tag);
                    return dto;
                })
                .toList();
    }
}
