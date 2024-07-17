package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoCreateDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoUpdateDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.entity.PlaylistVideoEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.mapper.PlaylistVideoInfoMapper;
import you_tube_own.repository.PlaylistVideoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PLaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;

    public String create(PlaylistVideoCreateDto dto) {
        var entity = PlaylistVideoEntity.builder()
                .playlistId(dto.getPlaylistId())
                .videoId(dto.getVideoId())
                .orderNumber(dto.getOrderNumber())
                .build();

        playlistVideoRepository.save(entity);
        return entity.getId();
    }

    public PlaylistVideoDto update(String id, PlaylistVideoUpdateDto dto) {
        PlaylistVideoEntity entity = get(id);

        entity.setPlaylistId(dto.getPlaylistId() == null ? entity.getPlaylistId() : dto.getPlaylistId());
        entity.setVideoId(dto.getVideoId() == null ? entity.getVideoId() : dto.getVideoId());
        entity.setOrderNumber(dto.getOrderNumber() == null ? entity.getOrderNumber() : dto.getOrderNumber());

        playlistVideoRepository.save(entity);

        return PlaylistVideoDto.builder()
                .id(entity.getId())
                .videoId(entity.getVideoId())
                .playlistId(entity.getPlaylistId())
                .orderNumber(entity.getOrderNumber())
                .build();
    }

    public String delete(String playlistId, String videoId) {
        int effectedRows = playlistVideoRepository.deleteByPlaylistIdAndVideoId(playlistId, videoId);
        if (effectedRows == 0) {
            throw new AppBadException("not deleted playlist video");
        }
        return "deleted playlist video";
    }

    public Page<PlaylistVideoDto> getByPlaylistId(Long playlistId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PlaylistVideoInfoMapper> page = playlistVideoRepository.findAllByPlaylistId(playlistId,pageable);

        List<PlaylistVideoDto> list = page.getContent()
                .stream()
                .map(this::fullInfo)
                .toList();

        long totalElements = page.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public PlaylistVideoDto fullInfo(PlaylistVideoInfoMapper entity){
        PlaylistVideoDto dto = new PlaylistVideoDto();
        dto.setPlaylistId(entity.getPlaylistId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setOrderNumber(entity.getOrderNumber());

        // create chanel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getChanelName());
        dto.setChanel(chanel);

        // create video
        VideoDto video = new VideoDto();
        video.setId(entity.getVideoId());
        video.setPreviewAttachId(entity.getPreviewAttachId());
        video.setTitle(entity.getVideoTitle());
        dto.setVideo(video);

        return dto;
    }

    public PlaylistVideoEntity get(String id) {
       return playlistVideoRepository.findById(id)
                .orElseThrow(()-> new AppBadException("Playlist video not found"));
    }
}
