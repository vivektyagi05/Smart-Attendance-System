package com.smart.attendance.config;

import com.smart.attendance.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Passwords ko secure hash karne ke liye
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // API based apps mein CSRF disable rakhte hain
            .cors(cors -> cors.configure(http)) // Frontend integration ke liye
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No cookies, only JWT
            .authorizeHttpRequests(auth -> auth
                // 1. In URLs ko koi bhi access kar sakta hai
                .requestMatchers("/", "/login.html", "/api/auth/**", "/static/**", "/css/**", "/js/**").permitAll()
                
                // 2. Sirf ADMIN access kar sakega
                .requestMatchers("/admin-panel.html").hasAuthority("ADMIN")
                
                // 3. Sirf USER (Employee) access kar sakega
                .requestMatchers("/dashboard.html").hasAuthority("USER")
                
                // 4. Baki saari APIs login mangengi
                .anyRequest().authenticated()
            )
            // Hamara Custom JWT Filter default security se pehle chalega
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}