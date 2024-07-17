package you_tube_own.mapper;

import you_tube_own.enums.ReportType;

import java.time.LocalDateTime;

public interface ReportInfoMapper {
    String getId();

    String getContent();

    String getEntityId();

    ReportType getType();

    LocalDateTime getCreatedDate();

    Long getProfileId();

    String getProfileName();

    String getProfileSurname();

    String getProfilePhotoId();
}
