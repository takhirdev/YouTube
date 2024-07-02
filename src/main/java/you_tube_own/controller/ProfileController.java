package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.profile.ProfileCreateDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.profile.ProfileUpdateDto;
import you_tube_own.service.ProfileService;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        String response = profileService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/change/email")
    public ResponseEntity<String> changeEmail(@RequestParam String newEmail) {
        String response = profileService.changeEmail(newEmail);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        String response = profileService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update/detail")
    public ResponseEntity<ProfileDto> update(@RequestBody ProfileUpdateDto dto) {
        ProfileDto response = profileService.update(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping(value = "/update/photo/{photoId}")
    public ResponseEntity<Boolean> update(@PathVariable String photoId) {
        Boolean response = profileService.updatePhoto(photoId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/getAll")
    public ResponseEntity<Page<ProfileDto>> getAll(@RequestParam int pageNumber,
                                                   @RequestParam int pageSize) {
        Page<ProfileDto> response = profileService.getAll(pageNumber - 1, pageSize);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/create/profile")
    public ResponseEntity<ProfileDto> createProfile(@Valid @RequestBody ProfileCreateDto dto) {
        ProfileDto response = profileService.createProfile(dto);
        return ResponseEntity.ok(response);
    }
}
