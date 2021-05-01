package com.example.mfa.security.services;

import com.example.mfa.security.datas.dtos.CustomUserDetails;
import com.example.mfa.security.datas.entities.UserEntity;
import com.example.mfa.security.exception.OtpNotProveException;
import com.example.mfa.security.util.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserService userService;
    private final MfaService mfaService;
    private String otp;

    @Autowired
    public CustomUserDetailsServiceImpl(UserService userService, MfaService mfaService) {
        this.userService = userService;
        this.mfaService = mfaService;
    }

    @Override
    public UserDetails loadUserByUsername(String username, String otp) throws UsernameNotFoundException, OtpNotProveException {
        this.otp = otp;

        if(otp != null){
            String scretKey = mfaService.getMfa(username).getSecretKey();
            if(!OTPUtil.checkCode(otp, scretKey)){
                throw new OtpNotProveException("OTP number didn't prove. Please check again.");
            }
        }

        return loadUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userService.getUser(UserEntity.builder().username(username).build());
        if(userEntity == null){
            throw new UsernameNotFoundException("The user not exist. Please check again.");
        }

        CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder();
        customUserDetailsBuilder
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(Arrays.stream(userEntity.getRoles().split(",")).map(x-> new SimpleGrantedAuthority(x)).collect(Collectors.toList()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true);

        return customUserDetailsBuilder.build();
    }
}
