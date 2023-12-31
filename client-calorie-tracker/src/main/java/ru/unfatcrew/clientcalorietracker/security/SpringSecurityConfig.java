package ru.unfatcrew.clientcalorietracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    public void disableEraseCredentials(AuthenticationManagerBuilder builder) {
        builder.eraseCredentials(false);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/resources/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(customizer -> customizer
                        .loginPage("/login")
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/")
                        .permitAll());

        return httpSecurity.build();
    }
}
