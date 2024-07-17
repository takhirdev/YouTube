package you_tube_own.dto.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import you_tube_own.enums.VidoeType;

import java.util.List;

@Data
public class VideoCreateDto {

    private String previewAttachId;

    @NotBlank(message = "title required")
    private String title;

    private String description;

    @NotNull(message = "category id required")
    private Integer categoryId;

    @NotBlank(message = "attach id required")
    private String attachId;

    @NotNull(message = "video type required")
    private VidoeType type;

    @NotNull(message = "chanel id required")
    private String chanelId;

    private List<String> tagList;

    private String playlistId;
}
