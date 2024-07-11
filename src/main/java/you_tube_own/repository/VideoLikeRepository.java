package you_tube_own.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.VideoLikeEntity;
import you_tube_own.enums.ReactionType;
import you_tube_own.mapper.VideoLikeMapper;
import java.util.Optional;

public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity, String> {

    Optional<VideoLikeEntity> findByVideoIdAndProfileId(String videoId, Long profileId);

    @Query( " SELECT " +
            " vl.id AS id, " +
            " v.id AS videoId," +
            " v.title AS videoTitle," +
            " v.previewAttachId AS previewAttachId, " +
            " ch.id AS chanelId," +
            " ch.name AS chanelName " +
            " FROM VideoLikeEntity AS vl " +
            " INNER JOIN vl.video AS v " +
            " INNER JOIN v.chanel AS ch " +
            " WHERE vl.profileId = ?1 " +
            " AND vl.reaction = ?2 ")
    Page<VideoLikeMapper> findAllByProfileIdAndReaction(Long profileId, ReactionType reaction, Pageable pageable);
}
