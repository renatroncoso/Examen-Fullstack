package cl.duoc.itinerary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva CSRF
            .csrf(AbstractHttpConfigurer::disable)
            
            // Sesión stateless (sin estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configura autorización
            .authorizeHttpRequests(auth -> auth
                // Swagger y OpenAPI (público)
                .requestMatchers(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/webjars/**"
                ).permitAll()
                // Actuator (público)
                .requestMatchers("/actuator/**").permitAll()
                // TODOS los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Desactiva login por formulario y HTTP Basic
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            
            // Agrega el filtro JWT antes del filtro de autenticación
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}