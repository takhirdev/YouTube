package you_tube_own.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoTagCreateDto {
    @NotBlank(message = "video id required")
    private String videoId;

    @NotBlank(message = "tag id required")
    private String tagId;
}
