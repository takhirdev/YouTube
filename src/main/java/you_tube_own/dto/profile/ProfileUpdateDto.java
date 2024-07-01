package you_tube_own.dto.profile;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDto {
    @Size(min = 4, max = 20)
    private String name;
    @Size(min = 4, max = 20)
    private String surname;
}
