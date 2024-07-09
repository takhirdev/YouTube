package you_tube_own.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfoMapper {
    public Long getPlaylistId();
    public String getVideoId();
    public String getPreviewAttachId();
    public String getVideoTitle();
    public String getChanelId();
    public String getChanelName();
    public LocalDateTime getCreatedDate();
    public Integer getOrderNumber();
}
