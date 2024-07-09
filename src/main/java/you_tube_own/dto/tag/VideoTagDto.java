package you_tube_own.dto.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.video.VideoDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoTagDto {
    private String id;
    private String videoId;
    private VideoDto video;
    private String tagId;
    private TagDto tag;
    private LocalDateTime createdDate;
}
