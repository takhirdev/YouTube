package you_tube_own.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.SubscriptionEntity;
import you_tube_own.mapper.SubscriptionMapper;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, String> {

    Optional<SubscriptionEntity> findByChanelIdAndProfileId(String channelId, Long profileId);

    @Query( " select " +
            " s.id as id," +
            " s.createdDate as createdDate, " +
            " ch.id as chanelId, " +
            " ch.name as chanelName, " +
            " p.id as chanelPhotoId " +
            " from SubscriptionEntity as s " +
            " inner join s.chanel as ch " +
            " inner join ch.photo as p " +
            " where s.profileId = ?1 ")
    List<SubscriptionMapper> findAllByProfileId(Long profileId);

}
