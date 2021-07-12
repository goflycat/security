package com.example.mfa.security.configures;

import com.example.mfa.security.configures.filter.CustomUsernamePasswordAuthenticationFilter;
import com.example.mfa.security.configures.filter.PreUsernamePasswordAuthenticationFilter;
import com.example.mfa.security.configures.handler.FailureHandler;
import com.example.mfa.security.configures.handler.LogoutSucceedHandler;
import com.example.mfa.security.configures.handler.SuccessHandler;
import com.example.mfa.security.configures.provider.CustomDaoAuthenticationProvider;
import com.example.mfa.security.services.MfaService;
import com.example.mfa.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
//@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableJpaRepositories(basePackages = {"com.example.mfa.security.repositories"})
@EntityScan(basePackages = {"com.example.mfa.security.datas"}, basePackageClasses = {Jsr310Converters.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final String permitallUrls = "/login,/,/prelogin,/mfactor,/purelogin";
    private final String authenticateUrls = "/*,/**,/**/*";
    private final UserService userService;
    private final MfaService mfaService;

    @Autowired
    public WebSecurityConfiguration(UserDetailsService userDetailsService, UserService userService, MfaService mfaService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.mfaService = mfaService;
    }



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/i18n/**")
                .antMatchers("/static/**")
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new PreUsernamePasswordAuthenticationFilter(bCryptPasswordEncoder(), userService, mfaService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(customDaoAuthenticationProvider());
        http
                .cors()
                .and().headers()
                .frameOptions()
                .sameOrigin()
                .and().authorizeRequests().antMatchers(permitallUrls.split(",")).permitAll()
                .and().formLogin().loginPage("/login").successHandler(new SuccessHandler()).failureHandler(new FailureHandler())
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSucceedHandler()).invalidateHttpSession(false).permitAll()
                .and().authorizeRequests().anyRequest().authenticated();

        http.csrf().disable();
    }

    private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(this.authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new FailureHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider(){
        CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider(userDetailsService);
        customDaoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return customDaoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
