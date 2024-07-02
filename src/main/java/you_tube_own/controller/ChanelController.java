package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.chanel.ChanelCreateDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.chanel.ChanelUpdateDto;
import you_tube_own.enums.Status;
import you_tube_own.service.ChanelService;

import java.util.List;

@RestController
@RequestMapping("/chanel")
@RequiredArgsConstructor
public class ChanelController {
    private final ChanelService chanelService;

    @PostMapping("/create")
    public ResponseEntity<ChanelDto> create(@Valid @RequestBody ChanelCreateDto dto) {
        ChanelDto response = chanelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{chanelId}")
    public ResponseEntity<ChanelDto> update(@PathVariable String chanelId,
                                            @Valid @RequestBody ChanelUpdateDto dto) {
        ChanelDto response = chanelService.update(chanelId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/photo/{chanelId}")
    public ResponseEntity<String> updatePhoto(@PathVariable String chanelId,
                                              @RequestParam String photoId) {
        chanelService.updatePhoto(chanelId, photoId);
        return ResponseEntity.status(HttpStatus.OK).body("chanel photo successfully updated");
    }

    @PutMapping("/update/photo/{chanelId}")
    public ResponseEntity<String> updateBanner(@PathVariable String chanelId,
                                               @RequestParam String bannerId) {
        chanelService.updateBanner(chanelId, bannerId);
        return ResponseEntity.status(HttpStatus.OK).body("chanel banner successfully updated");
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<ChanelDto>> getAll(@RequestParam int pageNumber,
                                                  @RequestParam int pageSize) {
        Page<ChanelDto> response = chanelService.getAll(pageNumber-1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byId/{chanelId}")
    public ResponseEntity<ChanelDto> getById(@PathVariable String chanelId) {
       ChanelDto response = chanelService.getById(chanelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/change/status/{chanelId}")
    public ResponseEntity<String> changeStatus(@PathVariable String chanelId,
                                               @RequestParam Status status) {
        chanelService.changeStatus(chanelId, status);
        return ResponseEntity.status(HttpStatus.OK).body("chanel status successfully changed");
    }

    @GetMapping("/user/chanels")
    public ResponseEntity<List<ChanelDto>> getUserChanels() {
       List<ChanelDto> response = chanelService.getUserChanels();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
