package kz.soft.finalrpo.service;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jakarta.persistence.EntityNotFoundException;
import kz.soft.finalrpo.dto.admin.CreateUserByAdminRequest;
import kz.soft.finalrpo.dto.user.UserResponse;
import kz.soft.finalrpo.entity.Role;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.UserMapper;
import kz.soft.finalrpo.repository.RoleRepository;
import kz.soft.finalrpo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class   AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;



    @Test
    void changeUserRole_shouldUpdateRole() {
        // given
        Long userId = 10L;
        String roleName = "ROLE_TEACHER";

        Role role = new Role();
        role.setId(2L);
        role.setName(roleName);

        User user = new User();
        user.setId(userId);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        adminService.changeUserRole(userId, roleName);

        // then
        assertNotNull(user.getRole());
        assertEquals(roleName, user.getRole().getName());
        verify(userRepository).save(user);
    }

    @Test
    void createUserByAdmin_shouldCreateNewUser() {
        // given
        CreateUserByAdminRequest request = new CreateUserByAdminRequest();
        request.setFullName("Ернар Сатыбалды");
        request.setEmail("ernar.student@gmail.com");
        request.setPassword("Student123!");
        request.setRole("ROLE_STUDENT");
        request.setEnabled(true);

        Role role = new Role();
        role.setId(3L);
        role.setName("ROLE_STUDENT");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED");

        User savedUser = new User();
        savedUser.setId(99L);
        savedUser.setEmail(request.getEmail());
        savedUser.setFullName(request.getFullName());
        savedUser.setRole(role);
        savedUser.setEnabled(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = mock(UserResponse.class);
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        // when
        UserResponse result = adminService.createUserByAdmin(request);

        // then
        assertNotNull(result);

        verify(userRepository).existsByEmail("ernar.student@gmail.com");
        verify(roleRepository).findByName(anyString());
        verify(passwordEncoder).encode("Student123!");
        verify(userRepository).save(any(User.class));
        verify(userMapper).toResponse(any(User.class));
    }




    @Test
    void createUserByAdmin_shouldThrowIfRoleNotFound() {
        // given
        CreateUserByAdminRequest request = new CreateUserByAdminRequest();
        request.setFullName("Test");
        request.setEmail("test@gmail.com");
        request.setPassword("123");
        request.setRole("ROLE_UNKNOWN");
        request.setEnabled(true);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        // when + then
        assertThrows(EntityNotFoundException.class, () -> adminService.createUserByAdmin(request));
    }
}
