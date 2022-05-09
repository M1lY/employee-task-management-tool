package io.github.M1lY.employeetaskmanagementtool.service;

import io.github.M1lY.employeetaskmanagementtool.dto.AuthenticationResponse;
import io.github.M1lY.employeetaskmanagementtool.dto.LoginRequest;
import io.github.M1lY.employeetaskmanagementtool.dto.RegisterRequest;
import io.github.M1lY.employeetaskmanagementtool.exceptions.EmployeeManagementException;
import io.github.M1lY.employeetaskmanagementtool.model.JwtBlackList;
import io.github.M1lY.employeetaskmanagementtool.model.NotificationEmail;
import io.github.M1lY.employeetaskmanagementtool.model.User;
import io.github.M1lY.employeetaskmanagementtool.model.VerificationToken;
import io.github.M1lY.employeetaskmanagementtool.repository.JwtBlackListRepository;
import io.github.M1lY.employeetaskmanagementtool.repository.UserRepository;
import io.github.M1lY.employeetaskmanagementtool.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtBlackListRepository jwtBlackListRepository;

    private final JwtDecoder jwtDecoder;

    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setRole("USER");
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(),"Click link to activate your Account: "+
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new EmployeeManagementException("Invalid Token"));
        enableUser(verificationToken.get());
    }

    private void enableUser(VerificationToken verificationToken) {
        Long userId = verificationToken.getUser().getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new EmployeeManagementException("User not found with userId: " + userId));
        user.setEnabled(true);

        verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
                () -> new EmployeeManagementException("User: " + loginRequest.getUsername()+" not found"));
        if(!user.isEnabled()){
            throw new EmployeeManagementException("User not activated");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .expiresDate(jwtDecoder.decode(token).getExpiresAt())
//                .expiresDate(Instant.now().plus(jwtProvider.getJwtExpirationTime(), ChronoUnit.HOURS))
                .username(loginRequest.getUsername())
                .role(user.getRole())
                .build();
    }

    public void logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ","");
        Instant expire = (Instant) jwtDecoder.decode(token).getClaims().get("exp");
        jwtBlackListRepository.save(JwtBlackList.builder()
                .token(token)
                .expire(expire)
                .build());
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    @Async
    public void deleteExpiredTokens(){
        Instant now = Instant.now();
        log.info("Deleting expires tokens: " + jwtBlackListRepository.countAllByExpireLessThan(now));
        jwtBlackListRepository.deleteAllByExpireLessThan(now);
    }
}
