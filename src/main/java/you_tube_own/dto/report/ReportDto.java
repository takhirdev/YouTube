package you_tube_own.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.enums.ReportType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private String id;
    private ProfileDto profile;
    private String content;
    private String entityId;
    private ReportType type;
    private LocalDateTime createdDate;
}
