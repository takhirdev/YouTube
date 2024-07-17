package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.report.ReportCreateDto;
import you_tube_own.dto.report.ReportDto;
import you_tube_own.entity.ReportEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.mapper.ReportInfoMapper;
import you_tube_own.repository.ReportRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public String createReport(ReportCreateDto dto) {
        var entity = ReportEntity.builder()
                .profileId(SecurityUtil.getProfileId())
                .content(dto.getContent())
                .entityId(dto.getEntityId())
                .type(dto.getType())
                .build();
        reportRepository.save(entity);
        return entity.getId();
    }

    public PageImpl<ReportDto> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReportInfoMapper> entityPage = reportRepository.findAll(pageable);
        List<ReportDto> list = entityPage.getContent()
                .stream()
                .map(this::reportInfo)
                .toList();

        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public void delete(String id) {
        ReportEntity reportEntity = get(id);
        reportRepository.delete(reportEntity);
    }

    public ReportEntity get(String id) {
        return reportRepository.findById(id)
                .orElseThrow(()-> new AppBadException("Report not found"));
    }

    public ReportDto reportInfo(ReportInfoMapper mapper) {
        ReportDto dto = new ReportDto();
        dto.setId(mapper.getId());
        dto.setEntityId(mapper.getEntityId());
        dto.setType(mapper.getType());
        dto.setContent(mapper.getContent());
        dto.setCreatedDate(mapper.getCreatedDate());

        // create profile
        ProfileDto profile=new ProfileDto();
        profile.setId(mapper.getProfileId());
        profile.setName(mapper.getProfileName());
        profile.setSurname(mapper.getProfileSurname());
        profile.setPhotoId(mapper.getProfilePhotoId());

        dto.setProfile(profile);
        return dto;
    }

    public List<ReportDto> getByUserId(Long userId) {
        return reportRepository.getByUserId(userId)
                .stream()
                .map(this::reportInfo)
                .toList();
    }
}
