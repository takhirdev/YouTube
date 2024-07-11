package you_tube_own.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "username is required")
    @Size(min = 5,  message = "username must be at least 5 characters")
    private String username;

    @Size(min = 4, message = "password must be at least 4 characters")
    @NotBlank(message = "password is required")
    private String password;
}
