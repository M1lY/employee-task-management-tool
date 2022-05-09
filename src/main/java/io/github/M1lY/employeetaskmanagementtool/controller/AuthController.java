package io.github.M1lY.employeetaskmanagementtool.controller;

import io.github.M1lY.employeetaskmanagementtool.dto.AuthenticationResponse;
import io.github.M1lY.employeetaskmanagementtool.dto.LoginRequest;
import io.github.M1lY.employeetaskmanagementtool.dto.RegisterRequest;
import io.github.M1lY.employeetaskmanagementtool.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity<>("Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Verification Successful", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        authService.logout(request);
        return new ResponseEntity<>("Logout Successful", HttpStatus.OK);
    }
}
