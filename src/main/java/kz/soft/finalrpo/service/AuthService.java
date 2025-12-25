package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.auth.ChangePasswordRequest;
import kz.soft.finalrpo.dto.auth.CreateTeacherRequest;
import kz.soft.finalrpo.dto.auth.RegisterRequest;
import kz.soft.finalrpo.entity.Role;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.repository.RoleRepository;
import kz.soft.finalrpo.repository.UserRepository;
import kz.soft.finalrpo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerStudent(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found!"));

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(studentRole)
                .build();

        userRepository.save(user);
    }

    public void createTeacher(CreateTeacherRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Role teacherRole = roleRepository.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("ROLE_TEACHER not found!"));

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(teacherRole)
                .build();

        userRepository.save(user);
    }

    public void changePassword(CustomUserDetails currentUser, ChangePasswordRequest request) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
