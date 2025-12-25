package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.user.UpdateProfileRequest;
import kz.soft.finalrpo.dto.user.UserProfileResponse;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.UserMapper;
import kz.soft.finalrpo.repository.UserRepository;
import kz.soft.finalrpo.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getMyProfile_shouldReturnMappedResponse() {
        // given
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        User user = new User();
        user.setId(userId);
        user.setFullName("Айбар Нұрбек");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserProfileResponse mapped = mock(UserProfileResponse.class);
        when(userMapper.toProfileResponse(user)).thenReturn(mapped);

        // when
        UserProfileResponse result = userService.getMyProfile(currentUser);

        // then
        assertNotNull(result);
        assertSame(mapped, result);

        verify(userRepository).findById(userId);
        verify(userMapper).toProfileResponse(user);
    }

    @Test
    void updateProfile_shouldUpdateFullName() {
        // given
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Updated Name");

        User user = new User();
        user.setId(userId);
        user.setFullName("Old Name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        userService.updateProfile(currentUser, request);

        // then
        assertEquals("Updated Name", user.getFullName());
        verify(userRepository).save(user);
    }

    @Test
    void setEnabled_shouldUpdateUserStatus() {
        // given
        Long userId = 10L;

        User user = new User();
        user.setId(userId);
        user.setEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        userService.setEnabled(userId, false);

        // then
        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }
}
