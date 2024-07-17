package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import you_tube_own.dto.category.CategoryDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playList.PlayListDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoCreateDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.video.VideoCreateDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.dto.video.VideoUpdateDto;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.entity.VideoEntity;
import you_tube_own.enums.ProfileRole;
import you_tube_own.enums.VideoStatus;
import you_tube_own.exception.AppBadException;
import you_tube_own.mapper.VideoFullInfoMapper;
import you_tube_own.mapper.VideoShortInfoForAdmin;
import you_tube_own.mapper.VideoShortInfoMapper;
import you_tube_own.repository.VideoRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoTagService videoTagService;

    public String create(VideoCreateDto dto) {
        var entity = VideoEntity.builder()
                .previewAttachId(dto.getPreviewAttachId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .attachId(dto.getAttachId())
                .type(dto.getType())
                .chanelId(dto.getChanelId())
                .build();

        videoRepository.save(entity);
        videoTagService.merge(entity.getId(),dto.getTagList());
        return entity.getId();
    }

    public VideoDto update(String videoId, VideoUpdateDto dto) {
        isOwner(videoId);

        VideoEntity entity = get(videoId);
        entity.setTitle(dto.getTitle() == null ? entity.getTitle() : dto.getTitle());
        entity.setDescription(dto.getDescription() == null ? entity.getDescription() : dto.getDescription());
        videoRepository.save(entity);

        return VideoDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
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
        Page<VideoShortInfoMapper> entityPage = videoRepository.findByCategoryId(categoryId, pageable);

        List<VideoDto> list = entityPage.getContent()
                .stream()
                .map(entity -> shortInfo(entity, false))
                .toList();

        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public Page<VideoDto> getByTitle(String title, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VideoShortInfoMapper> entityPage = videoRepository.findByTitle(title, pageable);

        List<VideoDto> list = entityPage.getContent()
                .stream()
                .map(entity -> shortInfo(entity, false))
                .toList();

        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

//    public Page<VideoDto> getByTagId(String tagId, int pageNumber, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        Page<VideoShortInfoMapper> entityPage = videoRepository.findByTagId(tagId, pageable);
//        List<VideoDto> list = entityPage.getContent()
//                .stream()
//                .map(entity -> shortInfo(entity, false))
//                .toList();
//
//        long totalElements = entityPage.getTotalElements();
//        return new PageImpl<>(list, pageable, totalElements);
//    }

    public VideoDto getById(String videoId) {
        VideoFullInfoMapper video = videoRepository.getByVideoId(videoId)
                .orElseThrow(() -> new AppBadException("video not found"));

        if (video.getStatus().equals(VideoStatus.PRIVATE)) {
            isOwnerOrAdmin(videoId);
        }
        return fullInfo(video);
    }

    public Page<VideoDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VideoShortInfoForAdmin> page = videoRepository.findAll(pageable);
        List<VideoDto> list = page.getContent()
                .stream()
                .map(entity -> shortInfo(entity, true))
                .toList();

        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public Page<VideoDto> getAllByChanelId(String chanelId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<VideoEntity> page = videoRepository.findAllByChanelId(chanelId,pageable);
        List<VideoDto> list = page.getContent()
                .stream()
                .map(entity -> {
                    return VideoDto.builder()
                            .id(entity.getId())
                            .title(entity.getTitle())
                            .description(entity.getDescription())
                            .previewAttachId(entity.getPreviewAttachId())
                            .viewCount(entity.getViewCount())
                            .publishedDate(entity.getPublishedDate())
                            .build();
                }).toList();

        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public VideoEntity get(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new AppBadException("Video not found"));
    }

    private VideoDto fullInfo(VideoFullInfoMapper entity) {
        // create chanel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getChanelName());
        chanel.setPhotoId(entity.getChanelPhotoId());

        // create category
        CategoryDto category = new CategoryDto();
        category.setId(entity.getCategoryId());
        category.setName(entity.getCategoryName());
                                                  // tagList =============
        // create video
        VideoDto video = new VideoDto();
        video.setId(entity.getId());
        video.setTitle(entity.getTitle());
        video.setDescription(entity.getDescription());
        video.setCategory(category);
        video.setChanel(chanel);
        video.setPreviewAttachId(entity.getPreviewAttachId());
        video.setAttachId(entity.getAttachId());
        video.setPublishedDate(entity.getPublishDate());
        video.setViewCount(entity.getViewCount());
        video.setSharedCount(entity.getSharedCount());
        video.setLikeCount(entity.getLikeCount());
        video.setDislikeCount(entity.getDislikeCount());
        return video;
    }

    private VideoDto shortInfo(VideoShortInfoForAdmin entity, boolean isAdmin) {
        // create chanel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getChanelName());
        chanel.setPhotoId(entity.getChanelPhotoId());

        // create video
        VideoDto dto = new VideoDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPreviewAttachId(entity.getPreviewAttachId());
        dto.setPublishedDate(entity.getPublishDate());
        dto.setChanel(chanel);
        dto.setViewCount(entity.getViewCount());

        if (isAdmin) {
            // create profile
            ProfileDto profile = new ProfileDto();
            profile.setId(entity.getProfileId());
            profile.setName(entity.getProfileName());
            profile.setSurname(entity.getProfileSurname());
            dto.setProfile(profile);

            // create playlist
            PlayListDto playList = new PlayListDto();
            playList.setId(entity.getPlaylistId());
            playList.setName(entity.getPlaylistName());
            dto.setPlayList(playList);
        }

        return dto;
    }

    private void isOwner(String videoId) {
        Long ownerId = videoRepository.findOwnerIdByVideoId(videoId);
        Long currentUserId = SecurityUtil.getProfileId();
        if (!ownerId.equals(currentUserId)) {
            throw new AppBadException("No access to update this video");
        }
    }

    private void isOwnerOrAdmin(String videoId) {
        Long ownerId = videoRepository.findOwnerIdByVideoId(videoId);
        ProfileEntity currentUser = SecurityUtil.getProfile();
        if (!ownerId.equals(currentUser.getId())
                && !currentUser.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("No access to get this video");
        }
    }
}
