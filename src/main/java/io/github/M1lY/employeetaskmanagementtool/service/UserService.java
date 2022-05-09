package io.github.M1lY.employeetaskmanagementtool.service;

import io.github.M1lY.employeetaskmanagementtool.dto.UserResponse;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import io.github.M1lY.employeetaskmanagementtool.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllByRoleAndEnabledTrue("USER")
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .role(user.getRole())
                .build();
    }

}
