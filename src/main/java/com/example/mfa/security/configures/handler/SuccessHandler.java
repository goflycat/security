package com.example.mfa.security.configures.handler;

import com.example.mfa.security.datas.dtos.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String successUrl = "/main";
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("password");
        request.getSession().removeAttribute("mfa");
        this.setDefaultTargetUrl(successUrl);
        HttpSession httpSession = request.getSession();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        customUserDetails.setPassword(null);
        httpSession.setAttribute("userInfo", customUserDetails);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

