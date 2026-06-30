package cl.duoc.itinerary.service;

import cl.duoc.itinerary.client.TravelerClient;
import cl.duoc.itinerary.dto.ItineraryDTO;
import cl.duoc.itinerary.model.Itinerary;
import cl.duoc.itinerary.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelerClient travelerClient;

    public Itinerary createItinerary(ItineraryDTO itineraryDTO) {
        log.info("Creando itinerario para travelerId: {}", itineraryDTO.getTravelerId());
        
        // Validar que el viajero existe
        if (!travelerClient.validateTraveler(itineraryDTO.getTravelerId())) {
            throw new RuntimeException("El viajero con ID " + itineraryDTO.getTravelerId() + " no existe");
        }
        
        // Calcular días totales
        long days = ChronoUnit.DAYS.between(itineraryDTO.getStartDate(), itineraryDTO.getEndDate());
        
        Itinerary itinerary = new Itinerary();
        itinerary.setTravelerId(itineraryDTO.getTravelerId());
        itinerary.setName(itineraryDTO.getName());
        itinerary.setStartDate(itineraryDTO.getStartDate());
        itinerary.setEndDate(itineraryDTO.getEndDate());
        itinerary.setDescription(itineraryDTO.getDescription());
        itinerary.setTotalDays((int) days + 1);
        itinerary.setIsActive(true);
        
        return itineraryRepository.save(itinerary);
    }

    public Itinerary updateItinerary(Long id, ItineraryDTO itineraryDTO) {
        log.info("Actualizando itinerario con id: {}", id);
        
        Itinerary itinerary = getItineraryById(id);
        
        itinerary.setName(itineraryDTO.getName());
        itinerary.setStartDate(itineraryDTO.getStartDate());
        itinerary.setEndDate(itineraryDTO.getEndDate());
        itinerary.setDescription(itineraryDTO.getDescription());
        
        // Recalcular días
        long days = ChronoUnit.DAYS.between(itineraryDTO.getStartDate(), itineraryDTO.getEndDate());
        itinerary.setTotalDays((int) days + 1);
        
        return itineraryRepository.save(itinerary);
    }

    public void deleteItinerary(Long id) {
        log.info("Eliminando itinerario con id: {}", id);
        Itinerary itinerary = getItineraryById(id);
        itineraryRepository.delete(itinerary);
    }

    public Itinerary getItineraryById(Long id) {
        return itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario no encontrado con id: " + id));
    }

    public List<Itinerary> getItinerariesByTraveler(Long travelerId) {
        log.info("Buscando itinerarios para travelerId: {}", travelerId);
        return itineraryRepository.findByTravelerId(travelerId);
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    public Itinerary activateItinerary(Long id) {
        Itinerary itinerary = getItineraryById(id);
        itinerary.setIsActive(true);
        return itineraryRepository.save(itinerary);
    }

    public Itinerary deactivateItinerary(Long id) {
        Itinerary itinerary = getItineraryById(id);
        itinerary.setIsActive(false);
        return itineraryRepository.save(itinerary);
    }
}