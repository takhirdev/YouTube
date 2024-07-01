package you_tube_own.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRegistrDto {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "surname is required")
    private String surname;
    @Email(message = "incorrect email format")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
    private String mainPhotoUrl;
}
