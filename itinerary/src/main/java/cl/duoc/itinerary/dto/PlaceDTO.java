package cl.duoc.itinerary.dto;

import cl.duoc.itinerary.model.PlaceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    private Long id;

    @NotBlank(message = "El nombre del lugar es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @NotNull(message = "El tipo de lugar es obligatorio")
    private PlaceType placeType;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String address;

    @FutureOrPresent(message = "La fecha de visita debe ser hoy o en el futuro")
    private LocalDate visitDate;

    @Min(value = 1, message = "El orden de visita debe ser mayor a 0")
    private Integer visitOrder;

    @DecimalMin(value = "0.5", message = "Las horas estimadas deben ser al menos 0.5")
    @DecimalMax(value = "24.0", message = "Las horas estimadas no pueden superar 24")
    private Double estimatedHours;

    @Pattern(regexp = "^[+]?[0-9\\s-]{8,15}$", message = "Formato de teléfono inválido")
    private String contactPhone;

    @Pattern(regexp = "^(http|https)://.*$", message = "URL inválida")
    private String website;
}