package com.example.mfa.security.services;

import com.example.mfa.security.datas.entities.UserEntity;
import com.example.mfa.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUser(UserEntity userEntity){
        return userRepository.findByUsername(userEntity.getUsername());
    }

}
