package com.example.mfa.security.services;

import com.example.mfa.security.exception.OtpNotApproveException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username, String otp) throws UsernameNotFoundException, OtpNotApproveException;
}
