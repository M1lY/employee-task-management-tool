package io.github.M1lY.employeetaskmanagementtool.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.M1lY.employeetaskmanagementtool.repository.JwtBlackListRepository;
import io.github.M1lY.employeetaskmanagementtool.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JwtBlackListRepository jwtBlackListRepository;
//    private AuthService authService;
//    private final JWTFilter jwtFilter = new JWTFilter(authService.jwtBlackListRepository);

    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    private JWTFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JWTFilter(jwtBlackListRepository), UsernamePasswordAuthenticationFilter.class);
        http.cors().and().csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/api/auth/logout")
                        .hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                        .antMatchers("/api/auth/**")
                        .permitAll()
                        .antMatchers(HttpMethod.GET,"/api/tasks")
                        .hasAnyAuthority("SCOPE_USER")
                        .antMatchers(HttpMethod.POST, "/api/tasks/create")
                        .hasAnyAuthority("SCOPE_ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/tasks/id/{taskId}")
                        .hasAnyAuthority("SCOPE_ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/tasks/all")
                        .hasAnyAuthority("SCOPE_ADMIN")
                        .antMatchers(HttpMethod.GET,"/api/tasks/user/{username}")
                        .hasAnyAuthority("SCOPE_ADMIN")
                        .antMatchers(HttpMethod.POST,"/api/tasks/status/{taskId}/{statusId}")
                        .hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER")
                        .antMatchers(HttpMethod.GET,"/api/users/all")
                        .hasAnyAuthority("SCOPE_ADMIN")
                        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()
//                        .hasRole("USER")
//                        .hasAuthority("ROLE_USER")
//                        .hasAuthority("SCOPE_ROLE_USER")
//                        .hasAuthority("USER")
//                        .permitAll()
//                        .antMatchers(HttpMethod.GET,"/api/tasks/**")
//                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
