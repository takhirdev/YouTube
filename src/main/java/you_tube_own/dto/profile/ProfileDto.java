package you_tube_own.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.dto.attach.AttachDto;
import you_tube_own.enums.ProfileRole;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private AttachDto photo;
    private String photoId;
    private LocalDateTime createdDate;
    private ProfileRole role;
    private String jwt;
}
