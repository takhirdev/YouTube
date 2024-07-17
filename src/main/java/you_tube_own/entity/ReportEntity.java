package you_tube_own.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import you_tube_own.enums.ReportType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report")
public class ReportEntity {

    @Id
    @UuidGenerator
    String id;

    @Column(name = "profile_id")
    private Long profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "entity_id")
    private String entityId;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Builder.Default
    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Builder.Default
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();
}
