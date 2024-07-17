package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.subscription.SubscriptionDto;
import you_tube_own.entity.SubscriptionEntity;
import you_tube_own.enums.Status;
import you_tube_own.exception.AppBadException;
import you_tube_own.mapper.SubscriptionMapper;
import you_tube_own.repository.SubscriptionRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public String create(String chanelId) {
        var entity = SubscriptionEntity
                .builder()
                .chanelId(chanelId)
                .profileId(SecurityUtil.getProfileId())
                .build();
        subscriptionRepository.save(entity);
        return entity.getId();
    }

    public SubscriptionDto updateStatus(String channelId, Status status) {
        SubscriptionEntity entity = getByChannelId(channelId);
        entity.setStatus(status);
        subscriptionRepository.save(entity);
        return toDto(entity);
    }

    public List<SubscriptionDto> getAllSubscriptions(Long userId) {
      return subscriptionRepository.findAllByProfileId(userId)
              .stream()
              .map(this::subscriptionInfo)
              .toList();
    }

    public SubscriptionEntity getByChannelId(String channelId) {
        Long profileId = SecurityUtil.getProfileId();
        return subscriptionRepository.findByChanelIdAndProfileId(channelId,profileId)
                .orElseThrow(()-> new AppBadException("subscription not found"));
    }

    private SubscriptionDto toDto(SubscriptionEntity entity) {
        return SubscriptionDto
                .builder()
                .id(entity.getId())
                .profileId(entity.getProfileId())
                .channelId(entity.getChanelId())
                .createdDate(entity.getCreatedDate())
                .unsubscribeDate(entity.getUnsubscribeDate())
                .subscriptionStatus(entity.getStatus())
                .build();
    }
    private SubscriptionDto subscriptionInfo(SubscriptionMapper mapper) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(mapper.getId());

        // create chanel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(mapper.getChanelId());
        chanel.setName(mapper.getChannelName());
        chanel.setPhotoId(mapper.getChanelPhotoId());

        dto.setChannel(chanel);
        return dto;
    }


}
