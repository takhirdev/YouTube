package you_tube_own.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.CategoryDto;
import you_tube_own.service.CategoryService;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        CategoryDto response = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Integer id,
                                              @RequestBody CategoryDto dto) {
        CategoryDto response = categoryService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok(true);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getList")
    public ResponseEntity<Page<CategoryDto>> getList(@RequestParam(defaultValue = "1") int pageNumber,
                                                     @RequestParam(defaultValue = "5") int pageSize) {
        Page<CategoryDto> response = categoryService.getList(pageNumber - 1, pageSize);
        return ResponseEntity.ok(response);
    }
}
