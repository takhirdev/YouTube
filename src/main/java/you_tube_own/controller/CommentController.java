package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.comment.CommentCreateDto;
import you_tube_own.dto.comment.CommentDto;
import you_tube_own.service.CommentService;
import you_tube_own.util.SecurityUtil;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CommentCreateDto dto) {
        String response = commentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable String commentId,
                                             @RequestParam String content) {
        CommentDto response = commentService.update(commentId, content);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Boolean> delete(@PathVariable String commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<CommentDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                   @RequestParam(defaultValue = "5") int pageSize) {
        Page<CommentDto> response = commentService.getAll(pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getAllByUserId/{userId}")
    public ResponseEntity<Page<CommentDto>> getAllByUserId(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "1") int pageNumber,
                                                           @RequestParam(defaultValue = "5") int pageSize) {
        Page<CommentDto> response = commentService.getAllByUserId(userId, pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getAllByCurrentUserId")
    public ResponseEntity<Page<CommentDto>> getAllByCurrentUserId(@RequestParam(defaultValue = "1") int pageNumber,
                                                                  @RequestParam(defaultValue = "5") int pageSize) {
        Page<CommentDto> response = commentService.getAllByUserId(SecurityUtil.getProfileId(), pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getAllByVideoId/{videoId}")
    public ResponseEntity<Page<CommentDto>> getAllByVideoId(@PathVariable String videoId,
                                                            @RequestParam(defaultValue = "1") int pageNumber,
                                                            @RequestParam(defaultValue = "5") int pageSize) {
        Page<CommentDto> response = commentService.getAllByVideoId(videoId, pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getAllByVideoId/{commentid}")
    public ResponseEntity<Page<CommentDto>> getAllReplyByCommentId(@PathVariable String commentId,
                                                                   @RequestParam(defaultValue = "1") int pageNumber,
                                                                   @RequestParam(defaultValue = "5") int pageSize) {
        Page<CommentDto> response = commentService.getAllReplyByCommentId(commentId, pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
