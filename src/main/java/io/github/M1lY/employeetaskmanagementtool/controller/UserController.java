package io.github.M1lY.employeetaskmanagementtool.controller;

import io.github.M1lY.employeetaskmanagementtool.dto.UserResponse;
import io.github.M1lY.employeetaskmanagementtool.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }
}
