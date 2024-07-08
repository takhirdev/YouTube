package you_tube_own.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoMapper extends VideoShortInfoForAdmin {
    String getId();
    String getTitle();
    String getPreviewAttachId();
    LocalDateTime getPublishDate();
    String getChanelId();
    String getChanelName();
    String getChanelPhotoId();
    Integer getViewCount();
}
