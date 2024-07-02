package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import you_tube_own.dto.JwtDTO;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.profile.LoginDto;
import you_tube_own.dto.profile.ProfileRegistrDto;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.ProfileRole;
import you_tube_own.enums.Status;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.ProfileRepository;
import you_tube_own.util.JwtUtil;
import you_tube_own.util.MD5Util;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;
    private final EmailSenderService emailSenderService;
    private final EmailHistoryService emailHistoryService;

    public String  registration(ProfileRegistrDto dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            throw new AppBadException("Email already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(Status.BLOCK);
        ProfileEntity saved = profileRepository.save(entity);

        String token = JwtUtil.generateToken(saved.getId(), saved.getEmail(), saved.getRole());
        emailSenderService.sendEmail(token);
        return "To complete your registration please verify your email.";
    }

    public String verification(String token) {
        JwtDTO dto = JwtUtil.decode(token);
        ProfileEntity profile = profileRepository.findByEmailAndVisibleTrue(dto.getUsername())
                .orElseThrow(() -> new AppBadException("User not found"));

        emailHistoryService.isNotExpiredEmail(profile.getEmail());

        if (!profile.getStatus().equals(Status.BLOCK)) {
            throw new AppBadException("Registration not completed");
        }

        profileRepository.updateStatus(profile.getId(), Status.ACTIVE);
        return "registration finished successfully ";
    }

    public ProfileDto login(LoginDto dto) {
        ProfileEntity profile = profileRepository.findByEmailAndVisibleTrue(dto.getUsername())
                .orElseThrow(() -> new AppBadException("User not found"));

        if (!profile.getPassword().equals(MD5Util.getMd5(dto.getPassword()))) {
            throw new AppBadException("Wrong password");
        }

        if (!profile.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("status not active");
        }
        ProfileDto response = new ProfileDto();
        response.setId(profile.getId());
        response.setName(profile.getName());
        response.setSurname(profile.getSurname());
        response.setEmail(profile.getEmail());
        response.setRole(profile.getRole());
        response.setCreatedDate(profile.getCreatedDate());
        response.setJwt(JwtUtil.generateToken(profile.getId(),profile.getEmail(), profile.getRole()));
        return response;
    }
}
