package kz.soft.finalrpo.service;

import jakarta.persistence.EntityNotFoundException;
import kz.soft.finalrpo.dto.user.UpdateProfileRequest;
import kz.soft.finalrpo.dto.user.UserProfileResponse;
import kz.soft.finalrpo.dto.user.UserResponse;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.UserMapper;
import kz.soft.finalrpo.repository.UserRepository;
import kz.soft.finalrpo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void updateProfile(CustomUserDetails currentUser, UpdateProfileRequest request) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + currentUser.getId()));

        user.setFullName(request.getFullName());
        userRepository.save(user);
    }

    public User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new IllegalArgumentException("Unauthorized");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
    }

    public UserResponse setEnabled(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        user.setEnabled(enabled);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }
    public UserProfileResponse getMyProfile(CustomUserDetails currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + currentUser.getId()));
        return userMapper.toProfileResponse(user);
    }


}
