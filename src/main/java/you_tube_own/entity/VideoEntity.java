package you_tube_own.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import you_tube_own.enums.VideoStatus;
import you_tube_own.enums.VidoeType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column(name = "title")
    private String title;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VideoStatus status = VideoStatus.PRIVATE;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VidoeType type;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "dislike_count")
    private Integer dislikeCount = 0;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chanel_id", insertable = false, updatable = false)
    private ChanelEntity chanel;
    @Column(name = "chanel_id")
    private String chanelId;

//    private List<Integer> tagList;
}
