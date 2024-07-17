package you_tube_own.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.report.ReportCreateDto;
import you_tube_own.dto.report.ReportDto;
import you_tube_own.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<String> createReport(@RequestBody ReportCreateDto dto) {
        return ResponseEntity.ok(reportService.createReport(dto));
    }

//    id,profile(id,name,surname,photo(id,url)),content,
//    entity_id(channel/video)),type(channel,video)
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ReportDto>> pagination(@RequestParam(name = "page",defaultValue = "1") int page,
                                                          @RequestParam(name = "size",defaultValue = "3") int size) {
        PageImpl<ReportDto> responseDTOList=reportService.pagination(page-1,size);
        return ResponseEntity.ok(responseDTOList);
    }

    @DeleteMapping("/removeById/{id}")
    public ResponseEntity<String> getById(String id) {
        reportService.delete(id);
        return ResponseEntity.ok("report removed");
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<List<ReportDto>> getByUserId(Long userid) {
        List<ReportDto> responseDTOList=reportService.getByUserId(userid);
        return ResponseEntity.ok(responseDTOList);
    }
}
