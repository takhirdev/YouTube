package you_tube_own.dto.chanel;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChanelUpdateDto {
    @Size(min = 3, message = "name must be at least 3 characters")
    private String name;
    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;
}
