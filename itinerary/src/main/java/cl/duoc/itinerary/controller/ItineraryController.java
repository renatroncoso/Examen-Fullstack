package cl.duoc.itinerary.controller;

import cl.duoc.itinerary.dto.ApiResponse;
import cl.duoc.itinerary.dto.ItineraryDTO;
import cl.duoc.itinerary.dto.ItineraryResponseDTO;
import cl.duoc.itinerary.model.Itinerary;
import cl.duoc.itinerary.service.ItineraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/itinerary")
@RequiredArgsConstructor
@Slf4j
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItineraryResponseDTO>> create(
            @Valid @RequestBody ItineraryDTO itineraryDTO) {
        log.info("POST /api/v1/itinerary");
        Itinerary itinerary = itineraryService.createItinerary(itineraryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Itinerario creado", mapToResponse(itinerary)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryResponseDTO>> getById(@PathVariable Long id) {
        log.info("GET /api/v1/itinerary/{}", id);
        Itinerary itinerary = itineraryService.getItineraryById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Itinerario encontrado", mapToResponse(itinerary)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ItineraryDTO itineraryDTO) {
        log.info("PUT /api/v1/itinerary/{}", id);
        Itinerary itinerary = itineraryService.updateItinerary(id, itineraryDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Itinerario actualizado", mapToResponse(itinerary)));
    }

    private ItineraryResponseDTO mapToResponse(Itinerary itinerary) {
        ItineraryResponseDTO dto = new ItineraryResponseDTO();
        dto.setId(itinerary.getId());
        dto.setTravelerId(itinerary.getTravelerId());
        dto.setName(itinerary.getName());
        dto.setStartDate(itinerary.getStartDate());
        dto.setEndDate(itinerary.getEndDate());
        dto.setDescription(itinerary.getDescription());
        dto.setTotalDays(itinerary.getTotalDays());
        dto.setIsActive(itinerary.getIsActive());
        return dto;
    }
}