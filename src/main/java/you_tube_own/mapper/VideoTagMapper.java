package you_tube_own.mapper;

import java.time.LocalDateTime;

public interface VideoTagMapper {
    String getId();
    String getVideoId();
    String getTagId();
    String getTagName();
    LocalDateTime getTagCreatedDate();
}
