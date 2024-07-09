package you_tube_own.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.profile.LoginDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.video.VideoDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private String id;
    private ProfileDto profile;
    private Long profileId;
    private String content;
    private String videoId;
    private VideoDto video;
    private String replId;
    private LocalDateTime createdDate;
    private Integer likeCount;
    private Integer dislikeCount;
}
