package com.example.mfa.security.configures.filter;

import com.example.mfa.security.datas.dtos.MfaDto;
import com.example.mfa.security.datas.entities.UserEntity;
import com.example.mfa.security.services.MfaService;
import com.example.mfa.security.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class PreUsernamePasswordAuthenticationFilter implements Filter {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MfaService mfaService;

    public PreUsernamePasswordAuthenticationFilter(PasswordEncoder passwordEncoder, UserService userService, MfaService mfaService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.mfaService = mfaService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if(httpServletRequest.getServletPath().equals("/prelogin") && httpServletRequest.getMethod().equals("POST")){
            String username = httpServletRequest.getParameter("username");
            String password = httpServletRequest.getParameter("password");
            UserEntity userEntity = userService.getUser(UserEntity.builder().username(username).build());

            if(Optional.ofNullable(userEntity).isPresent() && Optional.ofNullable(userEntity.getUsername()).isPresent()){
                if(((BCryptPasswordEncoder)passwordEncoder).matches(password, userEntity.getPassword())){
                    httpServletRequest.getSession().setAttribute("username", username);
                    httpServletRequest.getSession().setAttribute("password", password);
                    MfaDto mfaDto = mfaService.getMfa(username);
                    if(Optional.ofNullable(mfaDto).isPresent() && Optional.ofNullable(mfaDto.getSecretKey()).isPresent()){

                        httpServletRequest.getSession().setAttribute("mfa", true);
                        httpServletResponse.sendRedirect("/mfactor");
                    }else{
                        httpServletRequest.getSession().setAttribute("mfa", false);
                        httpServletResponse.sendRedirect("/purelogin");
                    }
                }else{
                    ((HttpServletResponse) response).sendRedirect("/logout");
                }
            }else{
                ((HttpServletResponse) response).sendRedirect("/logout");
            }

        }else{
            chain.doFilter(request, response);
        }
    }
}
