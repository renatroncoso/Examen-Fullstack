package cl.duoc.itinerary.client;

import cl.duoc.itinerary.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelerClient {

    private final WebClient webClient;

    @Value("${services.traveler.url:http://localhost:9002}")
    private String travelerServiceUrl;

    @Value("${services.traveler.path:/api/v1/traveler/validate}")
    private String travelerValidatePath;

    /**
     * Valida si un viajero existe en el Traveler Service
     * @param travelerId ID del viajero a validar
     * @return true si el viajero existe, false en caso contrario
     */
    public boolean validateTraveler(Long travelerId) {
        log.info("Validando viajero con ID: {} en Traveler Service", travelerId);
        
        try {
            String url = travelerServiceUrl + travelerValidatePath + "/" + travelerId;
            log.debug("URL de validación: {}", url);
            
            ApiResponse<?> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> {
                        log.error("Error al validar viajero: {} - {}", 
                                clientResponse.statusCode().value(), 
                                clientResponse.statusCode().getReasonPhrase());
                        return Mono.error(new RuntimeException("Error al validar viajero"));
                    })
                    .bodyToMono(ApiResponse.class)
                    .block();

            if (response != null && response.getCode() == 200) {
                log.info("Viajero con ID: {} validado exitosamente", travelerId);
                return true;
            } else {
                log.warn("Viajero con ID: {} no encontrado", travelerId);
                return false;
            }
            
        } catch (WebClientResponseException e) {
            log.error("Error de comunicación con Traveler Service: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Viajero con ID: {} no existe en el sistema", travelerId);
            }
            return false;
        } catch (Exception e) {
            log.error("Error inesperado al validar viajero: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene información de un viajero del Traveler Service
     * @param travelerId ID del viajero
     * @return Datos del viajero o null si no existe
     */
    public Object getTravelerInfo(Long travelerId) {
        log.info("Obteniendo información del viajero: {}", travelerId);
        
        try {
            String url = travelerServiceUrl + travelerValidatePath + "/" + travelerId;
            
            ApiResponse<?> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();

            if (response != null && response.getCode() == 200) {
                log.info("Información del viajero obtenida exitosamente");
                return response.getData();
            }
            
        } catch (Exception e) {
            log.error("Error al obtener información del viajero: {}", e.getMessage());
        }
        
        return null;
    }
}