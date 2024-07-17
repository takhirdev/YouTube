package you_tube_own.mapper;

import java.time.LocalDateTime;

public interface SubscriptionMapper {
//    id,channel(id,name,photo(id,url)),notification_type
    String getId();
    String getChanelId();
    String getChannelName();
    String getChanelPhotoId();
    LocalDateTime getChanelCreatedDate();
}
