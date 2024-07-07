package you_tube_own.dto.video;

import lombok.Data;

@Data
public class VideoUpdateDto {
    private String title;
    private String description;
    private Integer categoryId;
}
