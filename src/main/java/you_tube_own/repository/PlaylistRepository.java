package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.PlayListEntity;
import you_tube_own.enums.PlayListStatus;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<PlayListEntity, Long> {
    @Transactional
    @Modifying
    @Query("update PlayListEntity set status = ?2 where id = ?1")
    void updateStatus(Long playlistId, PlayListStatus status);

    Page<PlayListEntity> findAllBy(Pageable pageable);


    //   id,name,description,status(private,public),order_num,
    //    channel(id,name,photo(id,url),
    //    profile(id,name,surname,photo(id,url)
    //    ))

//    @Query("select pl.id, pl.name,pl.description,pl.status, pl.orderNumber," +
//            " ch.id, ch.name, ch.photoId, " +
//            " p.id, p.name, p.surname, p.photoId " +
//            " From PlayListEntity  as pl " +
//            " inner join pl.chanel as ch " +
//            " inner join ch.profile as p " +
//            " ")
//    Page<PlayListInfoMapper> findAllChannel(Pageable pageable);

    @Query("select count (p) from PlayListEntity  p ")
    int videoCount();

    @Query(" SELECT playlist " +
            " FROM PlayListEntity AS playlist " +
            " JOIN FETCH playlist.chanel AS channel " +
            " JOIN FETCH channel.profile AS profile " +
            " WHERE profile.id = :userId")
    List<PlayListEntity> getByUserId(Long userId);

    List<PlayListEntity> findAllByChanelId(String chanelId);
}
