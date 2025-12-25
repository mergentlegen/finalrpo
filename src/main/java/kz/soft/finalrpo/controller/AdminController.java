package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.admin.CreateUserByAdminRequest;
import kz.soft.finalrpo.dto.user.UserResponse;
import kz.soft.finalrpo.service.AdminService;
import kz.soft.finalrpo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/users/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        adminService.changeUserStatus(id, enabled);
        return "User status updated!";
    }

    @PutMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        adminService.changeUserRole(id, role);
        return "User role updated!";
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(@Valid @RequestBody CreateUserByAdminRequest request) {
        return adminService.createUserByAdmin(request);
    }

    @PatchMapping("/users/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse blockUser(@PathVariable Long id) {
        return userService.setEnabled(id, false);
    }

    @PatchMapping("/users/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse unblockUser(@PathVariable Long id) {
        return userService.setEnabled(id, true);
    }


}
