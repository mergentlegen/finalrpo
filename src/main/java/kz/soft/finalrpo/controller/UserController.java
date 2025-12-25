package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.user.UpdateProfileRequest;
import kz.soft.finalrpo.dto.user.UserProfileResponse;
import kz.soft.finalrpo.security.CustomUserDetails;
import kz.soft.finalrpo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    public UserProfileResponse myProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return userService.getMyProfile(currentUser);
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                @RequestBody @Valid UpdateProfileRequest request) {
        userService.updateProfile(currentUser, request);
        return ResponseEntity.ok("Profile updated!");
    }
}
