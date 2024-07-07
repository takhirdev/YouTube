package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.video.VideoCreateDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.dto.video.VideoUpdateDto;
import you_tube_own.enums.VideoStatus;
import you_tube_own.service.VideoService;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<String> createVideo(@Valid @RequestBody VideoCreateDto dto) {
        String response = videoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/detail/{videoId}")
    public ResponseEntity<VideoDto> updateDetail(@PathVariable String videoId,
                                                 @Valid @RequestBody VideoUpdateDto dto) {
        VideoDto response = videoService.update(videoId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/status/{videoId}")
    public ResponseEntity<String> updateStatus(@PathVariable String videoId,
                                               @RequestParam VideoStatus status) {
        String response = videoService.updateStatus(videoId,status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/increase/viewCount/{videoId}")
    public void increaseViewCount(@PathVariable String videoId) {
        videoService.increaseViewCount(videoId);
    }

    @GetMapping("/byCategoryId/{categoryId}")
    public ResponseEntity<Page<VideoDto>> getByCategoryId(@PathVariable int categoryId,
                                                @RequestParam(defaultValue = "1") int pageNumber,
                                                @RequestParam(defaultValue = "5") int pageSize) {
        Page<VideoDto> response = videoService.getByCategoryId(categoryId,pageNumber-1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byTitle")
    public ResponseEntity<Page<VideoDto>> getByTitle(@RequestParam String title,
                                                     @RequestParam(defaultValue = "1") int pageNumber,
                                                     @RequestParam(defaultValue = "5") int pageSize) {
        Page<VideoDto> response = videoService.getByTitle(title,pageNumber-1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
