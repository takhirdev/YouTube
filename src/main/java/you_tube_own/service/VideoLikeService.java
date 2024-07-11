package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.dto.video.VideoLikeCreateDto;
import you_tube_own.dto.video.VideoLikeDto;
import you_tube_own.entity.VideoLikeEntity;
import you_tube_own.enums.ReactionType;
import you_tube_own.mapper.VideoLikeMapper;
import you_tube_own.repository.VideoLikeRepository;
import you_tube_own.util.SecurityUtil;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;

    public void reaction(VideoLikeCreateDto dto) {
        Long profileId = SecurityUtil.getProfileId();
        Optional<VideoLikeEntity> optional = videoLikeRepository.findByVideoIdAndProfileId(dto.getVideoId(), profileId);

        if (optional.isEmpty()) {
            VideoLikeEntity entity = new VideoLikeEntity();
            entity.setVideoId(dto.getVideoId());
            entity.setProfileId(profileId);
            entity.setReaction(dto.getReaction());
            videoLikeRepository.save(entity);
            return;
        }

        VideoLikeEntity entity = optional.get();
        if (entity.getReaction().equals(dto.getReaction())) {
            videoLikeRepository.delete(entity);
            return;
        }

        entity.setReaction(dto.getReaction());
        videoLikeRepository.save(entity);
    }

    public Page<VideoLikeDto> getByUserId(Long profileId, ReactionType reaction, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate"));
        Page<VideoLikeMapper> pageEntity = videoLikeRepository.findAllByProfileIdAndReaction(profileId, reaction, pageable);
        List<VideoLikeDto> list = pageEntity.getContent()
                .stream()
                .map(entity ->{
                    // create chanel
                    ChanelDto chanelDto = new ChanelDto();
                    chanelDto.setId(entity.getChanelId());
                    chanelDto.setName(entity.getChanelName());

                    //create video
                    VideoDto videoDto = new VideoDto();
                    videoDto.setId(entity.getVideoId());
                    videoDto.setTitle(entity.getVideoTitle());
                    videoDto.setChanel(chanelDto);
                    videoDto.setPreviewAttachId(entity.getPreviewAttachId());

                    VideoLikeDto dto = new VideoLikeDto();
                    dto.setId(entity.getId());
                    dto.setVideo(videoDto);
                    return dto;
                })
                .toList();

        long totalElements = pageEntity.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }
}
