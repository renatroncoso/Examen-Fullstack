package cl.duoc.itinerary.client;

import cl.duoc.itinerary.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthClient {

    private final WebClient webClient;

    @Value("${services.auth.url:http://localhost:9000}")
    private String authServiceUrl;

    @Value("${services.auth.path:/api/v1/auth/validate}")
    private String authValidatePath;

    /**
     * Valida el token JWT con el Login Service
     * @param token Token JWT
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        log.info("Validando token con Login Service");
        
        try {
            String url = authServiceUrl + authValidatePath;
            
            ApiResponse<?> response = webClient.get()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> {
                        log.error("Error al validar token: {} - {}", 
                                clientResponse.statusCode().value(), 
                                clientResponse.statusCode().getReasonPhrase());
                        return Mono.error(new RuntimeException("Error al validar token"));
                    })
                    .bodyToMono(ApiResponse.class)
                    .block();

            if (response != null && response.getCode() == 200) {
                log.info("Token validado exitosamente");
                return true;
            } else {
                log.warn("Token inválido");
                return false;
            }
            
        } catch (WebClientResponseException e) {
            log.error("Error de comunicación con Login Service: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error inesperado al validar token: {}", e.getMessage());
            return false;
        }
    }
}