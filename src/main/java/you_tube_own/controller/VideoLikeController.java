package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import you_tube_own.dto.comment.CommentLikeCreateDto;
import you_tube_own.service.VideoLikeService;

@RestController
@RequestMapping("/video_like")
@RequiredArgsConstructor
public class VideoLikeController {
    private final VideoLikeService videoLikeService;

    @PostMapping("/like&dislike")
    public ResponseEntity reaction(@Valid @RequestBody CommentLikeCreateDto dto) {
//        videoLikeService.reaction(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
