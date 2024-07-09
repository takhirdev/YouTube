package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.playlistVideo.PlaylistVideoCreateDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoDto;
import you_tube_own.dto.playlistVideo.PlaylistVideoUpdateDto;
import you_tube_own.service.PLaylistVideoService;

@RestController
@RequestMapping("/playlist_video")
@RequiredArgsConstructor
public class PlaylistVideoController {

    private final PLaylistVideoService pLaylistVideoService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody PlaylistVideoCreateDto dto) {
        String response = pLaylistVideoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PlaylistVideoDto> update(@PathVariable String id,
                                                   @RequestBody PlaylistVideoUpdateDto dto) {
        PlaylistVideoDto response = pLaylistVideoService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long playlistId,
                                         @RequestParam String videoId) {
        String response = pLaylistVideoService.delete(playlistId, videoId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byPlaylistId")
    public ResponseEntity<Page<PlaylistVideoDto>> getByPlaylistId(@RequestParam Long playlistId,
                                                                  @RequestParam(defaultValue = "1") int pageNumber,
                                                                  @RequestParam(defaultValue = "5") int pageSize) {
        Page<PlaylistVideoDto> response = pLaylistVideoService.getByPlaylistId(playlistId, pageNumber-1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
