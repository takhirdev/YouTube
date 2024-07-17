package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import you_tube_own.dto.email.EmailHistoryDto;
import you_tube_own.dto.email.EmailHistoryFilterDto;
import you_tube_own.dto.email.FilterResponseDto;
import you_tube_own.entity.EmailHistoryEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.CustomEmailHistoryRepository;
import you_tube_own.repository.EmailHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;
    private final CustomEmailHistoryRepository emailHistoryCustomRepository;


    public void save(String email, String message) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(message);
        emailHistoryRepository.save(entity);
    }

    public Page<EmailHistoryDto> getAllByEmail(int pageNumber, int pageSize, String email) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate"));
        Page<EmailHistoryEntity> entityPage = emailHistoryRepository.findAllByEmail(email, pageable);

        List<EmailHistoryDto> list = entityPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        long totalElements = entityPage.getTotalElements();

        return new PageImpl<EmailHistoryDto>(list, pageable, totalElements);
    }


    public Page<EmailHistoryDto> pagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate"));
        Page<EmailHistoryEntity> entityPage = emailHistoryRepository.findAll(pageable);

        List<EmailHistoryDto> list = entityPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        long totalElements = entityPage.getTotalElements();

        return new PageImpl<EmailHistoryDto>(list, pageable, totalElements);
    }

    public Page<EmailHistoryDto> filter(EmailHistoryFilterDto dto, int page, Integer size) {
        FilterResponseDto<EmailHistoryEntity> filterResponse = emailHistoryCustomRepository.filter(dto, page, size);
        List<EmailHistoryDto> dtoList = filterResponse.getContent()
                .stream()
                .map(this::toDTO)
                .toList();
        Long totalCount = filterResponse.getTotalCount();
        return new PageImpl<>(dtoList,PageRequest.of(page,size),totalCount);
    }

    private EmailHistoryDto toDTO(EmailHistoryEntity entity) {
        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    public void isNotExpiredEmail(String email) {
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTop1ByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty()) {
            throw new AppBadException("Email history not found");
        }
        EmailHistoryEntity entity = optional.get();
        if (entity.getCreatedDate().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Confirmation time expired");
        }
    }
}
