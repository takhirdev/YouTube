package you_tube_own.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CategoryDto {

    private Integer id ;

    @NotBlank(message = "category name is required")
    private String name ;

    private LocalDateTime createdDate;
}
