package you_tube_own.repository;

import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.EmailHistoryEntity;

import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Long> {
    Optional<EmailHistoryEntity> findTop1ByEmailOrderByCreatedDateDesc(String email);
}
