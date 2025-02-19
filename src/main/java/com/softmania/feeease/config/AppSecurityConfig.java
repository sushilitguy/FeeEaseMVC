package com.softmania.feeease.config;

import com.softmania.feeease.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {
    @Autowired
    private UserDataService userService;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(customizer -> customizer.disable());
        security.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET,"/images/*","/","/fee_ease/register_school").permitAll()
                .anyRequest().authenticated());
        security.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/fee_ease/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll());
        security.logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll());
        return security.build();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}