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

public interface PlaylistRepository extends CrudRepository<PlayListEntity,Long> {
    @Transactional
    @Modifying
    @Query("update PlayListEntity set status = ?2 where id = ?1")
    void updateStatus(Long playlistId, PlayListStatus status);

    Page<PlayListEntity> findAllBy(Pageable pageable);

    @Query("select count (p) from PlayListEntity  p ")
    int videoCount();

    @Query( " SELECT playlist " +
            " FROM PlayListEntity AS playlist " +
            " JOIN FETCH playlist.chanel AS channel " +
            " JOIN FETCH channel.profile AS profile " +
            " WHERE profile.id = :userId")
    List<PlayListEntity> getByUserId(Long userId);

    List<PlayListEntity> findAllByChanelId(String chanelId);
}
