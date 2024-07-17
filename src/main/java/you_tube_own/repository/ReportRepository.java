package you_tube_own.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import you_tube_own.entity.ReportEntity;
import you_tube_own.mapper.ReportInfoMapper;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity,String> {

    @Query( "SELECT " +
            " r.id as id, " +
            " r.content as content, " +
            " r.entityId AS entityId," +
            " r.type as type, " +
            " r.createDate AS createdDate," +
            " p.id AS profileId," +
            " p.name as profileName, " +
            " p.surname as profileSurname, " +
            " p.photoId AS profilePhotoId " +
            " FROM ReportEntity AS r " +
            " INNER JOIN ProfileEntity AS p ON r.profileId=p.id ")
    Page<ReportInfoMapper> findAll(Pageable pageable);


    // 4. Report List By User id (ADMIN) ReportInfo
    @Query( " SELECT " +
            " r.id as id, " +
            " r.content as content, " +
            " r.entityId AS entityId," +
            " r.type as type, " +
            " r.createDate AS createdDate," +
            " p.id AS profileId," +
            " p.name as profileName, " +
            " p.surname as profileSurname, " +
            " p.photoId AS profilePhotoId " +
            " FROM ReportEntity AS r " +
            " INNER JOIN ProfileEntity AS p ON r.profileId=p.id " +
            " where p.id = ?1 ")
    List<ReportInfoMapper> getByUserId(Long profileId);
}
