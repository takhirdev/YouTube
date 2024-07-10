package you_tube_own.mapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import you_tube_own.dto.video.VideoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaylistShortInfoMapper {
    Long getPlaylistId();
    String getPlaylistName();
    LocalDateTime getPlaylistCreationDate();
    String getChanelId();
    String getChanelName();
    Integer getVideoCount();
    List<VideoDto> getVideos();
}
