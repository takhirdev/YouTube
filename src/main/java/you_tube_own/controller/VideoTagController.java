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

    @PostMapping("/create")
    public ResponseEntity<String> create (@Valid @RequestBody VideoTagCreateDto dto){
        String response = videoTagService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete (@RequestParam String videoId,
                                          @RequestParam String tagId){
        String response = videoTagService.delete(videoId,tagId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getTagByVideoId/{videoId}")
    public ResponseEntity<List<VideoTagDto>> getTagByVideoId (@PathVariable String videoId){
        List<VideoTagDto> response = videoTagService.getTagByVideoId(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
