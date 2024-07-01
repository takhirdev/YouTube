package you_tube_own.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.profile.LoginDto;
import you_tube_own.dto.profile.ProfileRegistrDto;
import you_tube_own.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registr")
    public ResponseEntity<String> registration(@Valid @RequestBody ProfileRegistrDto dto) {
        String response = authService.registration(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verification(@PathVariable String token) {
        String response = authService.verification(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDto> login(@Valid @RequestBody LoginDto dto) {
        ProfileDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
