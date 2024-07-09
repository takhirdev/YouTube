package you_tube_own.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.enums.ReactionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeDto {
    @NotBlank(message = "comment id required")
    private String commentId;
    @NotNull(message = "reaction required")
    private ReactionType reaction;
}
