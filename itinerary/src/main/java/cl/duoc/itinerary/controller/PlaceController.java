package cl.duoc.itinerary.controller;

import cl.duoc.itinerary.dto.ApiResponse;
import cl.duoc.itinerary.dto.PlaceDTO;
import cl.duoc.itinerary.model.Place;
import cl.duoc.itinerary.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/itinerary/{itineraryId}/places")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlaceDTO>> create(
            @PathVariable Long itineraryId,
            @Valid @RequestBody PlaceDTO placeDTO) {
        log.info("POST /api/v1/itinerary/{}/places", itineraryId);
        Place place = placeService.addPlaceToItinerary(itineraryId, placeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Lugar agregado", mapToDTO(place)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlaceDTO>>> getAll(@PathVariable Long itineraryId) {
        log.info("GET /api/v1/itinerary/{}/places", itineraryId);
        List<Place> places = placeService.getPlacesByItinerary(itineraryId);
        List<PlaceDTO> dtos = places.stream().map(this::mapToDTO).toList();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lugares encontrados", dtos));
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceDTO>> update(
            @PathVariable Long itineraryId,
            @PathVariable Long placeId,
            @Valid @RequestBody PlaceDTO placeDTO) {
        log.info("PUT /api/v1/itinerary/{}/places/{}", itineraryId, placeId);
        Place place = placeService.updatePlace(placeId, placeDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lugar actualizado", mapToDTO(place)));
    }

    private PlaceDTO mapToDTO(Place place) {
        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setPlaceType(place.getPlaceType());
        dto.setDescription(place.getDescription());
        dto.setAddress(place.getAddress());
        dto.setVisitDate(place.getVisitDate());
        dto.setVisitOrder(place.getVisitOrder());
        dto.setEstimatedHours(place.getEstimatedHours());
        dto.setContactPhone(place.getContactPhone());
        dto.setWebsite(place.getWebsite());
        return dto;
    }
}