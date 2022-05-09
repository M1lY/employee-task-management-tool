package io.github.M1lY.employeetaskmanagementtool.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    public String generateToken(Authentication authentication){
        return generateTokenWithUserName(authentication);
    }

    public String generateTokenWithUserName(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtExpirationTime, ChronoUnit.HOURS))
//                .expiresAt(Instant.now().plus(1, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
