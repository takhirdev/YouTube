package you_tube_own.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import you_tube_own.dto.AttachDto;
import you_tube_own.service.AttachService;

@RestController
@RequestMapping("/attach")
@RequiredArgsConstructor
public class AttachController {
    private final AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDto> upload(@RequestParam("file") MultipartFile file) {
        AttachDto response = attachService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping(value = "/byId/{attachId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> getById(@PathVariable String attachId) {
        byte[] response = attachService.getById(attachId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{attachId}")
    public ResponseEntity<Resource> download(@PathVariable String attachId) {
        Resource file = attachService.download(attachId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<AttachDto>> pagination(@RequestParam(defaultValue = "1") int pageNumber,
                                                      @RequestParam(defaultValue = "5") int pageSize) {
        Page<AttachDto> response = attachService.pagination(pageNumber-1,pageSize);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{attachId}")
    public ResponseEntity delete(@PathVariable String attachId) {
        attachService.delete(attachId);
        return ResponseEntity.ok("file deleted");
    }

}
