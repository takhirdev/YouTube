package you_tube_own.mapper;

import you_tube_own.enums.VideoStatus;

import java.time.LocalDateTime;

public interface VideoFullInfoMapper {
    String getId();
    String getTitle();
    String getDescription();
    VideoStatus getStatus();
    String getPreviewAttachId();
    String getAttachId();
    Integer getCategoryId();
    String getCategoryName();
    String getTagId();
    String getTagName();
    LocalDateTime getPublishDate();
    String getChanelId();
    String getChanelName();
    String getChanelPhotoId();
    Integer getViewCount();
    Integer getSharedCount();
    Integer getLikeCount();
    Integer getDislikeCount();
}
