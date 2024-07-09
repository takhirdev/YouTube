package you_tube_own.dto.chanel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.dto.attach.AttachDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChanelDto {
    private String id;
    private String name;
    private String description;
    private Status status;
    private String photoId;
    private AttachDto photo;
    private String bannerId;
    private AttachDto banner;
    private Long profileId;
    private ProfileDto profile;
    private LocalDateTime created;
}
