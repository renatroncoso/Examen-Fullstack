package cl.duoc.itinerary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryResponseDTO {

    private Long id;
    private Long travelerId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Integer totalDays;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PlaceDTO> places;
}