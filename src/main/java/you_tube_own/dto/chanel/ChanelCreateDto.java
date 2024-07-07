package you_tube_own.dto.chanel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChanelCreateDto {
    @NotBlank(message = "name required")
    @Size(min = 3, message = "name must be at least 3 characters")
    private String name;
    @NotBlank(message = "description required")
    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;
    private String bannerId;
    private String photoId;
}
