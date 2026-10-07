package com.angelchacon.pedidos_delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((req, res, ex) -> res.setStatus(HttpStatus.FORBIDDEN.value())))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/v1/auth/**").permitAll()

                        // Comercios y productos
                        .requestMatchers(HttpMethod.GET, "/api/v1/comercios/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/comercios/**").hasRole("ADMIN")

                        // Pedidos
                        .requestMatchers(HttpMethod.POST, "/api/v1/pedidos").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/mis-pedidos").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/disponibles").hasAnyRole("REPARTIDOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/pedidos/*/estado").hasAnyRole("REPARTIDOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/pedidos/*/cancelar").hasAnyRole("CLIENTE", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}