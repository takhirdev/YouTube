package you_tube_own.dto.chanel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChanelCreateDto {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "name is required")
    private String description;
    private String bannerId;
    private String photoId;
}
