package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.PlayListEntity;
import you_tube_own.enums.PlayListStatus;
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

    @Query(" select pl.id, pl.name,pl.description,pl.status, pl.orderNumber," +
            " ch.id, ch.name, ch.photoId, " +
            " p.id, p.name, p.surname, p.photoId " +
            " From PlayListEntity  as pl " +
            " inner join pl.chanel as ch " +
            " inner join ch.profile as p " +
            " ORDER BY pl.orderNumber DESC ")
    Page<PlaylistFullInfoMapper> findAllBy(Pageable pageable);

    @Query(" select count (p) from PlayListEntity  p ")
    int videoCount();

    @Query( " select pl.id, pl.name,pl.description,pl.status, pl.orderNumber," +
            " ch.id, ch.name, ch.photoId, " +
            " p.id, p.name, p.surname, p.photoId " +
            " From PlayListEntity  as pl " +
            " inner join pl.chanel as ch " +
            " inner join ch.profile as p "+
            " WHERE p.id = ?1" +
            " ORDER BY pl.orderNumber DESC ")
    List<PlaylistFullInfoMapper> getByUserId(Long userId);


//    @Query( " select pl.id, pl.name, " +
//            " ch.id, ch.name, (select count (T) from PlayListEntity as T) as videoCount " +
//            " From PlayListEntity  as pl " +
//            " inner join pl.chanel as ch " +
//            " WHERE ch.id = ?1 ")
    List<PlayListEntity> findAllByChanelId(String chanelId);
}
