package you_tube_own.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.TagEntity;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {
    Optional<TagEntity> findByName(String name);

    Page<TagEntity> findAllBy(Pageable pageable);
}
