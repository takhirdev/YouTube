package you_tube_own.repository;

import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.CommentLikeEntity;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, String> {

    Optional<CommentLikeEntity> findByCommentIdAndProfileId(String commentId, Long profileId);
}
