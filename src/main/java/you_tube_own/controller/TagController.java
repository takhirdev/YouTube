package you_tube_own.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.TagDto;
import you_tube_own.service.TagService;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<TagDto> create(@RequestBody TagDto dto) {
        TagDto response = tagService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<TagDto> update(@PathVariable Integer id, @RequestBody TagDto dto) {
        TagDto response = tagService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<Page<TagDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                               @RequestParam(defaultValue = "5") int pageSize) {
        Page<TagDto> response = tagService.getAll(pageNumber - 1, pageSize);
        return ResponseEntity.ok(response);
    }
}
