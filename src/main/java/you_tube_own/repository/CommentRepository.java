package you_tube_own.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import you_tube_own.entity.CommentEntity;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, String> {

    @Query( " SELECT c FROM CommentEntity AS c" +
            " JOIN FETCH c.video ")
    Page<CommentEntity> findAllBy(Pageable pageable);

    @Query( " SELECT c FROM CommentEntity AS c" +
            " JOIN FETCH c.video " +
            " WHERE c.profileId = ?1 ")
    Page<CommentEntity> findAllByProfileId(Long profileId, Pageable pageable);

    @Query("SELECT c FROM CommentEntity c WHERE c.id = :commentId AND c.profileId = :profileId")
    Optional<CommentEntity> findByIdAndProfileId(@Param("commentId") String commentId, @Param("profileId") Long profileId);

    @Query( " SELECT c FROM CommentEntity AS c" +
            " JOIN FETCH c.profile " +
            " WHERE c.videoId = ?1 ")
    Page<CommentEntity> findAllByVideoId(String videoId, Pageable pageable);

    @Query( " SELECT c FROM CommentEntity AS c" +
            " JOIN FETCH c.profile " +
            " WHERE c.replyId = ?1 ")
    Page<CommentEntity> findAllByReplyId(String replyId, Pageable pageable);
}
