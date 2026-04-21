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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

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
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Pehle bataya gaya CORS Bean add karein
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

            // PUBLIC UI
            .requestMatchers("/", "/login.html", "/dashboard.html", "/css/**", "/js/**").permitAll()

            // AUTH APIs
            .requestMatchers("/api/auth/**").permitAll()

            // FILES
            .requestMatchers("/uploads/**").permitAll()

            // ADMIN
            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

            // PROTECTED APIs
            .requestMatchers("/api/otp/**").authenticated()
            .requestMatchers("/api/dashboard/**").authenticated()
            .requestMatchers("/api/profile").authenticated()
            .requestMatchers("/api/documents/**").authenticated()
            .requestMatchers("/api/**").authenticated()
            .requestMatchers("/api/payslip/**").authenticated()

            // 👇 ONLY ONE ANY REQUEST (LAST LINE)
            .anyRequest().permitAll()
        )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Apne frontend ka URL yahan allow karein (e.g., Live Server port 5500)
        configuration.setAllowedOrigins(Arrays.asList("*"));        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/*

.authorizeHttpRequests(auth -> auth
                // 1. Pages ko permitAll() rakhein taaki UI load ho sake
                .requestMatchers("/", "/login.html", "/dashboard.html", "/css/**", "/js/**").permitAll()
                
                // 2. Auth APIs ko permitAll() karein
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/change-password").authenticated()
                
                // 3. Data APIs ko protect karein (Ye APIs hi JWT mangengi)
                .requestMatchers("/api/dashboard/**").authenticated() 
                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/api/profile").authenticated()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/documents/**").authenticated()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/attendance/**").permitAll()
                .requestMatchers("/api/otp/**").authenticated()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/api/payslip/**").authenticated()
                
                .anyRequest().permitAll()
            )

*/