package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.AttachDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.video.VideoCreateDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.dto.video.VideoUpdateDto;
import you_tube_own.entity.VideoEntity;
import you_tube_own.enums.VideoStatus;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.VideoRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

    public String create(VideoCreateDto dto) {
        VideoEntity saved = videoRepository.save(toEntity(dto));
        return saved.getId();
    }

    public VideoDto update(String videoId, VideoUpdateDto dto) {
        isOwner(videoId);

        VideoEntity entity = getById(videoId);
        entity.setTitle(dto.getTitle() == null ? entity.getTitle() : dto.getTitle());
        entity.setDescription(dto.getDescription() == null ? entity.getDescription() : dto.getDescription());
        VideoEntity updated = videoRepository.save(entity);
        return toDto(updated);
    }

    public VideoEntity getById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new AppBadException("Video not found"));
    }

    private VideoEntity toEntity(VideoCreateDto dto) {
        VideoEntity entity = new VideoEntity();
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setTitle(dto.getTitle());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        entity.setType(dto.getType());
        entity.setChanelId(dto.getChanelId());
        return entity;
    }


    public String updateStatus(String videoId, VideoStatus status) {
        isOwner(videoId);
        videoRepository.updateStatus(videoId, status);
        return "video status changed successfully";
    }

    public void increaseViewCount(String videoId) {
        videoRepository.increaseViewCount(videoId);
    }

    public Page<VideoDto> getByCategoryId(int categoryId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VideoEntity> entityPage = videoRepository.findByCategoryId(categoryId, pageable);

        List<VideoDto> list = entityPage.getContent()
                .stream()
                .map(this::shortInfo)
                .toList();

        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public Page<VideoDto> getByTitle(String title,int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VideoEntity> entityPage = videoRepository.findByTitle(title, pageable);

        List<VideoDto> list = entityPage.getContent()
                .stream()
                .map(this::shortInfo)
                .toList();

        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }
    
    private VideoDto shortInfo(VideoEntity entity) {
        // create preview attach
        AttachDto previewAttach = new AttachDto();
        previewAttach.setId(entity.getPreviewAttachId());
        previewAttach.setUrl(entity.getPreviewAttach().getPath() + entity.getPreviewAttachId());

        // create chanel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getChanel().getName());
              // create chanel photo
        AttachDto chanelPhoto = new AttachDto();
        chanelPhoto.setUrl(entity.getChanel().getPhoto().getPath() + entity.getChanel().getPhoto().getId());
        chanel.setPhoto(chanelPhoto);

        var dto = VideoDto
                .builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .previewAttach(previewAttach)
                .publishedDate(entity.getPublishedDate())
                .chanel(chanel)
                .viewCount(entity.getViewCount())
                .build();
        return dto;
    }

    private VideoDto toDto(VideoEntity entity) {
        var dto = VideoDto.builder()
                .id(entity.getId())
                .previewAttachId(entity.getPreviewAttachId())
                .title(entity.getTitle())
                .categoryId(entity.getCategoryId())
                .attachId(entity.getAttachId())
                .type(entity.getType())
                .chanelId(entity.getChanelId())
                .createdDate(entity.getCreatedDate())
                .status(entity.getStatus())
                .type(entity.getType())
                .viewCount(entity.getViewCount())
                .sharedCount(entity.getSharedCount())
                .likeCount(entity.getLikeCount())
                .dislikeCount(entity.getDislikeCount())
                .build();
        return dto;
    }


    private void isOwner(String videoId) {
        Long ownerId = videoRepository.findOwnerByVideoId(videoId);
        Long currentUserId = SecurityUtil.getProfileId();
        if (!ownerId.equals(currentUserId)) {
            throw new AppBadException("NO access to update this video");
        }
    }
}
