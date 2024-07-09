package you_tube_own.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.VideoTagEntity;
import you_tube_own.mapper.VideoTagMapper;

import java.util.List;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM VideoTagEntity AS v WHERE v.videoId = ?1 AND v.tagId = ?2")
    int deleteByVideoIdAndTagId(String videoId, String tagId);

    @Query(" SELECT v.id AS id, v.videoId AS videoId, t.id AS tagId, t.name AS tagName, v.createdDate AS createdDate " +
            " FROM VideoTagEntity  AS v " +
            " INNER JOIN v.tag AS t " +
            " WHERE v.videoId = ?1 ")
    List<VideoTagMapper> findAllByVideoId(String videoId);
}
