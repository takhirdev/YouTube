package you_tube_own.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TagDto {
    private Integer id ;

    @NotBlank(message = "Tag name is required")
    private String name ;

    private LocalDateTime createdDate;
}
