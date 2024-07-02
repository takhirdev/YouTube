package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/change/email")
    public ResponseEntity<String> changeEmail(@RequestParam String newEmail) {
        String response = profileService.changeEmail(newEmail);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        String response = profileService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update/detail")
    public ResponseEntity<ProfileDto> update(@RequestBody ProfileUpdateDto dto) {
        ProfileDto response = profileService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping(value = "/update/photo/{photoId}")
    public ResponseEntity<String> update(@PathVariable String photoId) {
        profileService.updatePhoto(photoId);
        return ResponseEntity.status(HttpStatus.OK).body("Photo updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/getAll")
    public ResponseEntity<Page<ProfileDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                   @RequestParam(defaultValue = "5") int pageSize) {
        Page<ProfileDto> response = profileService.getAll(pageNumber - 1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/current/profile")
    public ResponseEntity<ProfileDto> getCurrentProfile() {
        ProfileDto response = profileService.getCurrentProfile();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create/profile")
    public ResponseEntity<ProfileDto> createProfile(@Valid @RequestBody ProfileCreateDto dto) {
        ProfileDto response = profileService.createProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
