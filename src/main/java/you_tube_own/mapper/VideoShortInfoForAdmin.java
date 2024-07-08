package you_tube_own.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoForAdmin{
    String getId();
    String getTitle();
    String getPreviewAttachId();
    LocalDateTime getPublishDate();
    String getChanelId();
    String getChanelName();
    String getChanelPhotoId();
    Integer getViewCount();
    Long getProfileId();
    String getProfileName();
    String getProfileSurname();
    Long getPlaylistId();
    String getPlaylistName();
}
