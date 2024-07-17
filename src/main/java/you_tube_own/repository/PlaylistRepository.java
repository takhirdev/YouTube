package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.PlayListEntity;
import you_tube_own.enums.PlayListStatus;
import you_tube_own.mapper.PlayListMapper;
import you_tube_own.mapper.PlaylistFullInfoMapper;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<PlayListEntity, Long> {
    @Transactional
    @Modifying
    @Query("update PlayListEntity set status = ?2 where id = ?1")
    void updateStatus(Long playlistId, PlayListStatus status);

    @Query( " SELECT ch.profileId FROM PlayListEntity AS pl " +
            " INNER JOIN pl.chanel AS ch " +
            " WHERE pl.id = ?1")
    long findOwnerIdByPlaylistId(Long playlistId);

    @Query( " SELECT pl.id AS id," +
            " pl.name AS name, " +
            " pl.description AS description," +
            " pl.status AS status," +
            " pl.orderNumber AS orderNumber," +
            " pl.createdDate AS createdDate, " +
            " ch.id AS chanelId," +
            " ch.name AS chanelName," +
            " ch.photoId AS chanelPhotoId, " +
            " p.id AS profileId," +
            " p.name AS profileName," +
            " p.surname AS profileSurname," +
            " p.photoId AS profilePhotoId " +
            " FROM PlayListEntity  AS pl " +
            " INNER JOIN pl.chanel AS ch " +
            " INNER JOIN ch.profile AS p "+
            " ORDER BY pl.orderNumber DESC ")
    Page<PlaylistFullInfoMapper> findAllBy(Pageable pageable);


    @Query( " SELECT pl.id AS id," +
            " pl.name AS name, " +
            " pl.description AS description," +
            " pl.status AS status," +
            " pl.orderNumber AS orderNumber," +
            " pl.createdDate AS createdDate, " +
            " ch.id AS chanelId," +
            " ch.name AS chanelName," +
            " ch.photoId AS chanelPhotoId, " +
            " p.id AS profileId," +
            " p.name AS profileName," +
            " p.surname AS profileSurname," +
            " p.photoId AS profilePhotoId " +
            " FROM PlayListEntity  AS pl " +
            " INNER JOIN pl.chanel AS ch " +
            " INNER JOIN ch.profile AS p "+
            " WHERE p.id = ?1" +
            " ORDER BY pl.orderNumber DESC ")
    List<PlaylistFullInfoMapper> getByUserId(Long userId);

    @Query(" SELECT " +
            " p.id AS id, " +
            " p.name AS name, " +
            " COUNT(pv.videoId) AS videoCount, " +
            " SUM(v.viewCount) AS totalViewCount " +
            " FROM PlayListEntity AS p " +
            " INNER JOIN PlaylistVideoEntity pv ON p.id = pv.playlistId " +
            " INNER JOIN VideoEntity v ON pv.videoId = v.id " +
            " WHERE p.id = ?1 " +
            " GROUP BY p.id, p.name ")
    PlayListMapper getById(Long id);

    @Query( " SELECT " +
            " p.id AS playlistId, " +
            " p.name AS playlistName, " +
            " p.createdDate AS playlistCreationDate," +
            " ch.id AS chanelId, " +
            " ch.name AS chanelName, " +
            " COUNT(pv.videoId) AS videoCount, " +
            " json_agg(json_build_object( " +
            "        'id', v.id, " +
            "        'title', v.title, " +
            "        'viewCount', v.viewCount, " +
            "        'status', v.status " +
            "    )) AS videos " +
            " FROM PlayListEntity p " +
            " INNER JOIN p.chanel AS ch " +
            " INNER JOIN PlaylistVideoEntity pv ON p.id = pv.playlistId " +
            " INNER JOIN VideoEntity v ON pv.videoId = v.id " +
            " WHERE ch.id = ?1 " +
            " GROUP BY p.id, p.name, ch.id, ch.name ")
    List<Object[]> findAllByChanelId(String chanelId);

}
