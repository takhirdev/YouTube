package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.VideoEntity;
import you_tube_own.enums.VideoStatus;

import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoEntity, String> {

    @Query( " SELECT ch.profileId FROM VideoEntity AS v " +
            " INNER JOIN v.chanel AS ch " +
            " WHERE v.id = ?1")
    Long findOwnerByVideoId(String videoId);


    @Transactional
    @Modifying
    @Query("UPDATE VideoEntity SET status = ?2 WHERE id = ?1")
    void updateStatus(String videoId, VideoStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE VideoEntity SET viewCount = viewCount + 1 WHERE id = ?1")
    void increaseViewCount(String videoId);

    Page<VideoEntity> findByCategoryId(int categoryId, Pageable pageable);

    Page<VideoEntity> findByTitle(String title, Pageable pageable);
}
