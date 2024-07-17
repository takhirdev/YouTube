package you_tube_own.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.enums.Status;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private String id;
    private Long profileId;
    private ProfileDto profile;
    private String channelId;
    private ChanelDto channel;
    private LocalDateTime createdDate;
    private LocalDateTime unsubscribeDate;
    private Status subscriptionStatus;
//    private NotificationType notificationType;
}
