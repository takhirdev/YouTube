package you_tube_own.repository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.Status;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =?2 where id =?1")
    void updateStatus(Long profileId, Status status);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set email =?2 where id =?1")
    void updateEmail(Integer profileId, String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set photoId =?2 where id =?1")
    void updatePhoto(Long profileId, String photoId);

    boolean existsByEmail(String email);

    Page<ProfileEntity> findAllBy(Pageable pageable);
}
