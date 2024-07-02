package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.JwtDTO;
import you_tube_own.dto.profile.ProfileCreateDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.profile.ProfileUpdateDto;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.Status;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.ProfileRepository;
import you_tube_own.util.JwtUtil;
import you_tube_own.util.MD5Util;
import you_tube_own.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailSenderService emailSenderService;
    private final EmailHistoryService emailHistoryService;

    public String changePassword(String oldPassword, String newPassword) {
        ProfileEntity profile = SecurityUtil.getProfile();
        if (!profile.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("profile status not active");
        }
        if (!profile.getPassword().equals(MD5Util.getMd5(oldPassword))) {
            throw new AppBadException("wrong password");
        }
        profile.setPassword(MD5Util.getMd5(newPassword));
        profileRepository.save(profile);
        return "password changed";
    }

    public String changeEmail(String newEmail) {
        ProfileEntity profile = SecurityUtil.getProfile();

        if (!profile.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("profile status not active");
        }
        if (profile.getEmail().equals(newEmail) || profileRepository.existsByEmail(newEmail)) {
            throw new AppBadException("email already in use");
        }

        String token = JwtUtil.generateToken(profile.getId(), newEmail, profile.getRole());
        emailSenderService.sendEmailForChange(token);
        return "please verify your new email.";
    }

    public String verifyEmail(String token) {
        JwtDTO dto = JwtUtil.decode(token);
        String email = dto.getUsername();
        Integer profileId = dto.getId();

        emailHistoryService.isNotExpiredEmail(email);

        profileRepository.updateEmail(profileId, email);
        return "email changed successfully";
    }

    public ProfileDto update(ProfileUpdateDto dto) {
        ProfileEntity profile = SecurityUtil.getProfile();

        if (!profile.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("profile status not active");
        }

        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        ProfileEntity saved = profileRepository.save(profile);
        return toDto(saved);
    }

    public Boolean updatePhoto(String photoId) {
        ProfileEntity profile = SecurityUtil.getProfile();

        String oldPhoto = profile.getPhotoId();
        profile.setPhotoId(photoId);
        profileRepository.save(profile);
        ///////   delete old attach
        return true;
    }

    public Page<ProfileDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProfileEntity> entityPage = profileRepository.findAllBy(pageable);
        List<ProfileDto> list = entityPage
                .getContent()
                .stream()
                .map(this::toDto)
                .toList();


        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public ProfileDto createProfile(ProfileCreateDto dto) {
        if (profileRepository.existsByEmail(dto.getEmail())) {
            throw new AppBadException("email already in use");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setStatus(Status.ACTIVE);
        entity.setRole(dto.getRole());

        ProfileEntity saved = profileRepository.save(entity);
        return toDto(saved);
    }

    private ProfileDto toDto(ProfileEntity entity) {
        ProfileDto dto = new ProfileDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setMainPhotoUrl(entity.getPhotoId());
        return dto;
    }
}
