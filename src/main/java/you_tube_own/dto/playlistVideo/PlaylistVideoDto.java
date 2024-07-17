package you_tube_own.dto.playlistVideo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playList.PlayListDto;
import you_tube_own.dto.video.VideoDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDto {
    private String id;
    private String playlistId;
    private PlayListDto playList;
    private String videoId;
    private VideoDto video;
    private Integer orderNumber;
    private LocalDateTime createdDate;
    private ChanelDto chanel;
}
