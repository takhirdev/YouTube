package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import you_tube_own.dto.comment.CommentLikeDto;
import you_tube_own.service.CommentLikeService;

@RestController
@RequestMapping("/comment_like")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeservice;

    @PostMapping("/like&dislike")
    public ResponseEntity reaction (@Valid @RequestBody CommentLikeDto dto) {
        commentLikeservice.reaction(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
