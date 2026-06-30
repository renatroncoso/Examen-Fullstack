package cl.duoc.itinerary.config;

import cl.duoc.itinerary.client.AuthClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Saltar validación para endpoints públicos
        if (path.startsWith("/v3/api-docs") || 
            path.startsWith("/swagger-ui") || 
            path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                log.debug("Token extraído: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            }

            if (token != null && authClient.validateToken(token)) {
                // Token válido, autenticar usuario
                UserDetails userDetails = new User("user", "", new ArrayList<>());
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Token validado correctamente para: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
            } else {
                log.warn("Token inválido o no proporcionado para: {}", request.getRequestURI());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"code\":401,\"message\":\"No autorizado - Token inválido\",\"data\":null}");
            }
        } catch (Exception e) {
            log.error("Error en autenticación: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":401,\"message\":\"No autorizado - Error de autenticación\",\"data\":null}");
        }
    }
}