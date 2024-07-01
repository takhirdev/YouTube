package you_tube_own.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.AttachEntity;

import java.util.List;

public interface AttachRepository extends CrudRepository<AttachEntity, String> {
    Page<AttachEntity> findAllBy(Pageable pageable);
}
