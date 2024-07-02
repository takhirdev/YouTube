package you_tube_own.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.enums.ProfileRole;

@Getter
@Setter
public class ProfileCreateDto {
    @NotBlank(message = "name is required")
    @Size(min = 5, message = "name must be at least 5 characters")
    private String name;
    @Size(min = 5,  message = "surname must be at least 5 characters")
    @NotBlank(message = "surname is required")
    private String surname;
    @Email(message = "incorrect email format")
    @NotBlank(message = "email is required")
    private String email;
    @Size(min = 7, message = "password must be at least 7 characters")
    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "role is required")
    private ProfileRole role;
}
