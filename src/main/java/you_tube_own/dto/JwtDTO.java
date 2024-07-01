package you_tube_own.dto;

import lombok.Getter;
import lombok.Setter;
import you_tube_own.enums.ProfileRole;

@Getter
@Setter
public class JwtDTO {
    private Integer id;
    private String username;
    private ProfileRole role;

    public JwtDTO(Integer id) {
        this.id = id;
    }

    public JwtDTO(Integer id, String username, ProfileRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
