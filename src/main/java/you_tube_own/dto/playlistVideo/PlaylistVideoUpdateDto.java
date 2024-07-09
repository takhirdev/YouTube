package you_tube_own.dto.playlistVideo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistVideoUpdateDto {
    private Long playlistId;
    private String videoId;
    private Integer orderNumber;
}
