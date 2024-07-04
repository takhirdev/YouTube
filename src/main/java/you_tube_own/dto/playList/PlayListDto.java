package you_tube_own.dto.playList;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.enums.PlayListStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayListDto {
    private Long id;
    private String name;
    private String description;
    private PlayListStatus status;
    private String chanelId;
    private ChanelDto chanel;
    private Long profileId;
    private ProfileDto profile;
    private int orderNumber;
    private LocalDateTime createdDate;
    private Integer videoCount;
}
