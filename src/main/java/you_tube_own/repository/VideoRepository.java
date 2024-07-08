package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.VideoEntity;
import you_tube_own.enums.VideoStatus;
import you_tube_own.mapper.VideoFullInfoMapper;
import you_tube_own.mapper.VideoShortInfoForAdmin;
import you_tube_own.mapper.VideoShortInfoMapper;

import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoEntity, String> {

    @Query( " SELECT ch.profileId FROM VideoEntity AS v " +
            " INNER JOIN v.chanel AS ch " +
            " WHERE v.id = ?1")
    Long findOwnerIdByVideoId(String videoId);


    @Transactional
    @Modifying
    @Query("UPDATE VideoEntity SET status = ?2 WHERE id = ?1")
    void updateStatus(String videoId, VideoStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE VideoEntity AS  v SET  v.viewCount = COALESCE(v.viewCount,0) + 1 WHERE v.id = ?1")
    void increaseViewCount(String videoId);

    @Query(" SELECT v.id AS id, v.title AS title, v.previewAttachId AS previewAttachId, v.publishedDate AS publishDate, v.viewCount AS viewCount, " +
            " ch.id AS chanelId, ch.name AS chanelName, ch.photoId AS chanelPhotoId " +
            " FROM VideoEntity AS v " +
            " INNER JOIN v.chanel AS ch " +
            " WHERE v.categoryId = ?1 ")
    Page<VideoShortInfoMapper> findByCategoryId(int categoryId, Pageable pageable);

    @Query( " SELECT v.id AS id, v.title AS title, v.previewAttachId AS previewAttachId, v.publishedDate AS publishDate, v.viewCount AS viewCount, " +
            " ch.id AS chanelId, ch.name AS chanelName, ch.photoId AS chanelPhotoId " +
            " FROM VideoEntity AS v " +
            " INNER JOIN v.chanel AS ch " +
            " WHERE v.title = ?1 ")
    Page<VideoShortInfoMapper> findByTitle(String title, Pageable pageable);

//    @Query( " SELECT v.id, v.title, v.previewAttachId, v.publishedDate, v.viewCount, ch.id, ch.name" +
//            " FROM VideoEntity AS v " +
//            " INNER JOIN v.chanel AS ch " +
//            " WHERE v. = ?1 ")
//    Page<VideoShortInfoMapper> findByTagId(String title, Pageable pageable);

    @Query( " SELECT v.id AS id, v.title AS title, v.description AS description, v.previewAttachId AS previewAttachId, " +
            " v.attachId AS attachId, v.status AS status, v.publishedDate AS publishedDate, v.viewCount AS viewCount, " +
            " v.sharedCount AS sharedCount, v.likeCount AS likeCount, v.dislikeCount AS dislikeCount, " +
            " ch.id AS chanelId, ch.name AS chanelName, ch.photoId AS chanelPhotoId, " +
            " c.id AS categoryId, c.name AS categoryName " +
            " FROM VideoEntity v " +
            " INNER JOIN v.chanel ch " +
            " INNER JOIN v.category c " +
            " WHERE v.id = ?1")
    Optional<VideoFullInfoMapper> getByVideoId(String videoId);

    @Query( " SELECT v.id AS id, v.title AS title, v.previewAttachId AS previewAttachId, v.publishedDate AS publishDate, v.viewCount AS viewCount, " +
            " ch.id AS chanelId, ch.name AS chanelName, ch.photoId AS chanelPhotoId, " +
            " p.id AS profileId, p.name AS profileName, p.surname AS profileSurname, " +
            " pl.id AS playlistId, pl.name AS playlistName " +
            " FROM VideoEntity AS v " +
            " INNER JOIN v.chanel AS ch " +
            " INNER JOIN ch.playlists AS pl" +
            " INNER JOIN ch.profile AS p ")
    Page<VideoShortInfoForAdmin> findAll(Pageable pageable);

    Page<VideoEntity> findAllByChanelId(String chanelId, Pageable pageable);
}
