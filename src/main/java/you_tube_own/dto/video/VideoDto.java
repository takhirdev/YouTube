package you_tube_own.dto.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.attach.AttachDto;
import you_tube_own.dto.category.CategoryDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playList.PlayListDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.enums.VideoStatus;
import you_tube_own.enums.VidoeType;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDto {
    private String id;
    private String previewAttachId;
    private AttachDto previewAttach;
    private String title;
    private String attachId;
    private ProfileDto profile;
    private PlayListDto playList;
    private AttachDto attach;
    private CategoryDto category;
    private Integer categoryId;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private VideoStatus status;
    private VidoeType type;
    private Integer viewCount;
    private Integer sharedCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private String description;
    private ChanelDto chanel;
    private String chanelId;
}
