package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.tag.VideoTagCreateDto;
import you_tube_own.dto.tag.VideoTagDto;
import you_tube_own.service.VideoTagService;

import java.util.List;

@RestController
@RequestMapping("/video_tag")
@RequiredArgsConstructor
public class VideoTagController {
    private final VideoTagService videoTagService;

    @GetMapping("/getTagByVideoId/{videoId}")
    public ResponseEntity<List<VideoTagDto>> getTagByVideoId (@PathVariable String videoId){
        List<VideoTagDto> response = videoTagService.getTagByVideoId(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
