package kz.soft.finalrpo.service;

import jakarta.persistence.EntityNotFoundException;
import kz.soft.finalrpo.dto.admin.CreateUserByAdminRequest;
import kz.soft.finalrpo.dto.user.UserResponse;
import kz.soft.finalrpo.entity.Role;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.UserMapper;
import kz.soft.finalrpo.repository.RoleRepository;
import kz.soft.finalrpo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public void changeUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public void changeUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String normalized = roleName.startsWith("ROLE_")
                ? roleName
                : "ROLE_" + roleName;

        Role role = roleRepository.findByName(normalized)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + normalized));

        user.setRole(role);
        userRepository.save(user);
    }


    public UserResponse createUserByAdmin(CreateUserByAdminRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        String roleName = request.getRole().startsWith("ROLE_")
                ? request.getRole()
                : "ROLE_" + request.getRole();

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));


        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(request.isEnabled())
                .build();

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

}
