package you_tube_own.dto.playList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.enums.PlayListStatus;
@Setter
@Getter
public class PlayListUpdateDto {
    @Size(min = 3, message = "name must be at least 3 characters")
    private String name;

    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;

    private Integer orderNumber;

}
