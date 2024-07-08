package you_tube_own.entity;

import jakarta.persistence.*;
import lombok.*;
import you_tube_own.enums.PlayListStatus;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "playlist")
public class PlayListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chanel_id")
    private String chanelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chanel_id", insertable = false, updatable = false)
    private ChanelEntity chanel;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlayListStatus status;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
