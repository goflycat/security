package com.example.mfa.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SecurityApplicationTests {

    @Test
    void contextLoads(){
        System.out.format("password:[%s]\n", new BCryptPasswordEncoder().encode("security!#34"));
    }

}
