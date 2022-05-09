package io.github.M1lY.employeetaskmanagementtool.config;

import io.github.M1lY.employeetaskmanagementtool.repository.JwtBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends GenericFilter {
    private final JwtBlackListRepository jwtBlackListRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(request.getHeader(HttpHeaders.AUTHORIZATION)==null){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ","");

        if(jwtBlackListRepository.findByTokenEquals(token)!=null){
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Blacklisted token");
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
