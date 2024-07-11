package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.video.VideoLikeCreateDto;
import you_tube_own.dto.video.VideoLikeDto;
import you_tube_own.enums.ReactionType;
import you_tube_own.service.VideoLikeService;
import you_tube_own.util.SecurityUtil;


@RestController
@RequestMapping("/video_like")
@RequiredArgsConstructor
public class VideoLikeController {
    private final VideoLikeService videoLikeService;

    @PostMapping("/like&dislike")
    public ResponseEntity reaction(@Valid @RequestBody VideoLikeCreateDto dto) {
        videoLikeService.reaction(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getByCurrentUserId")
    public ResponseEntity<Page<VideoLikeDto>> getByCurrentUserId(@RequestParam ReactionType reaction,
                                                                 @RequestParam(defaultValue = "1") int pageNumber,
                                                                 @RequestParam(defaultValue = "5") int pageSize) {
        Page<VideoLikeDto> response = videoLikeService.getByUserId(SecurityUtil.getProfileId(), reaction, pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Page<VideoLikeDto>> getByUserId(@PathVariable long userId,
                                                          @RequestParam ReactionType reaction,
                                                          @RequestParam(defaultValue = "1") int pageNumber,
                                                          @RequestParam(defaultValue = "5") int pageSize) {
        Page<VideoLikeDto> response = videoLikeService.getByUserId(userId, reaction, pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
