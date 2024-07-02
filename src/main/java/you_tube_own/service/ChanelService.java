package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.chanel.ChanelCreateDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.chanel.ChanelUpdateDto;
import you_tube_own.entity.ChanelEntity;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.ProfileRole;
import you_tube_own.enums.Status;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.ChanelRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChanelService {
    private final ChanelRepository chanelRepository;
    private final AttachService attachService;

    public ChanelDto create(ChanelCreateDto dto) {
        ChanelEntity entity = toEntity(dto);
        ChanelEntity saved = chanelRepository.save(entity);
        return toDto(saved);
    }

    public ChanelDto update(String chanelId, ChanelUpdateDto dto) {
        isOwner(chanelId);     // check current user is OWNER this channel

        ChanelEntity chanelEntity = get(chanelId);
        if (dto.getName() != null) {
            chanelEntity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            chanelEntity.setDescription(dto.getDescription());
        }
        ChanelEntity saved = chanelRepository.save(chanelEntity);
        return toDto(saved);
    }

    public void updatePhoto(String chanelId, String newPhotoId) {
        isOwner(chanelId);      // check current user is OWNER this channel

        ChanelEntity chanelEntity = get(chanelId);
        String oldPhotoId = chanelEntity.getPhotoId();
        chanelRepository.updatePhotoId(newPhotoId, chanelId);

        if (oldPhotoId != null) {
            attachService.delete(oldPhotoId);
        }
    }

    public void updateBanner(String chanelId, String newBannerId) {
        isOwner(chanelId);      // check current user is OWNER this channel

        ChanelEntity chanelEntity = get(chanelId);
        String oldBannerId = chanelEntity.getBannerId();
        chanelRepository.updateBannerId(newBannerId, chanelId);

        if (oldBannerId != null) {
            attachService.delete(oldBannerId);
        }
    }

    public Page<ChanelDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ChanelEntity> entityPage = chanelRepository.findAllBy(pageable);
        List<ChanelDto> list = entityPage.getContent()
                .stream()
                .map(this::toDto)
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public void changeStatus(String chanelId, Status status) {
        isAdminOrOwner(chanelId);       // check current user is OWNER this channel or ADMIN
        chanelRepository.updateStatus(status, chanelId);
    }

    public List<ChanelDto> getUserChanels() {
        Long profileId = SecurityUtil.getProfileId();
        return chanelRepository.findAllByProfileId(profileId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ChanelDto getChanelById(String chanelId) {
        ChanelEntity entity = get(chanelId);
        return toDto(entity);
    }

    private ChanelEntity toEntity(ChanelCreateDto dto) {
        ChanelEntity chanelEntity = new ChanelEntity();
        chanelEntity.setName(dto.getName());
        chanelEntity.setDescription(dto.getDescription());
        chanelEntity.setStatus(Status.ACTIVE);
        chanelEntity.setBannerId(dto.getBannerId());
        chanelEntity.setPhotoId(dto.getPhotoId());
        chanelEntity.setProfileId(SecurityUtil.getProfileId());
        return chanelEntity;
    }

    private ChanelDto toDto(ChanelEntity entity) {
        ChanelDto dto = new ChanelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setBannerId(dto.getBannerId());
        dto.setPhotoId(dto.getPhotoId());
        dto.setProfileId(entity.getProfileId());
        dto.setCreated(entity.getCreated());
        return dto;
    }

    public ChanelEntity get(String chanelId) {
        return chanelRepository.findById(chanelId)
                .orElseThrow(() -> new RuntimeException("Chanel not found"));
    }

    private void isOwner(String chanelId) {
        Long profileId = SecurityUtil.getProfileId();
        ChanelEntity chanelEntity = get(chanelId);
        if (!chanelEntity.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("chanel not active");
        }

        if (!chanelEntity.getProfileId().equals(profileId)) {
            throw new AppBadException("You do not have permission to update");
        }
    }

    private void isAdminOrOwner(String chanelId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        ChanelEntity chanelEntity = get(chanelId);
        if (!chanelEntity.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("chanel not active");
        }

        if (!chanelEntity.getProfileId().equals(profile.getId())
                || !profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("You do not have permission to change");
        }
    }
}
