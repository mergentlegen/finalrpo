package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.auth.ChangePasswordRequest;
import kz.soft.finalrpo.dto.auth.CreateTeacherRequest;
import kz.soft.finalrpo.dto.auth.RegisterRequest;
import kz.soft.finalrpo.security.CustomUserDetails;
import kz.soft.finalrpo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegisterRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok("Student registered successfully!");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create-teacher")
    public ResponseEntity<String> createTeacher(@RequestBody @Valid CreateTeacherRequest request) {
        authService.createTeacher(request);
        return ResponseEntity.ok("Teacher created successfully!");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                 @RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(currentUser, request);
        return ResponseEntity.ok("Password changed successfully!");
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                "ID=" + currentUser.getId() +
                        ", EMAIL=" + currentUser.getUsername() +
                        ", ROLE=" + currentUser.getRoleName()
        );
    }
}
