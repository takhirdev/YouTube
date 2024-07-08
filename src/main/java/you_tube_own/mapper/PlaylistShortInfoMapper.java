package you_tube_own.mapper;

import you_tube_own.entity.VideoEntity;
import you_tube_own.enums.PlayListStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface PlaylistShortInfoMapper {
    long getId();
    String getName();
    LocalDateTime getCreatedDate();
    String getChanelId();
    String getChanelName();
    Integer getVideoCount();
    List<VideoEntity> getVideoList();
}
