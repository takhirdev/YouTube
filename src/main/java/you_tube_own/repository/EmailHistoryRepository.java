package you_tube_own.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.EmailHistoryEntity;

import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Long> {
    Optional<EmailHistoryEntity> findTop1ByEmailOrderByCreatedDateDesc(String email);

    Page<EmailHistoryEntity> findAllByEmail(String email, Pageable pageable);

    Page<EmailHistoryEntity> findAll(Pageable pageable);
}
