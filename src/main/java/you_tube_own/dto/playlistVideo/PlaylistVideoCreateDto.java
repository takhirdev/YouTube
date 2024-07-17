package you_tube_own.dto.playlistVideo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistVideoCreateDto {
    @NotNull(message = "playlist id required")
    private String playlistId;

    @NotBlank(message = "video id required")
    private String videoId;

    @NotNull(message = "order number required")
    private Integer orderNumber;
}
