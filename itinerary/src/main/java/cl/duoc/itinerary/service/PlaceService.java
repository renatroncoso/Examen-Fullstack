package cl.duoc.itinerary.service;

import cl.duoc.itinerary.dto.PlaceDTO;
import cl.duoc.itinerary.model.Itinerary;
import cl.duoc.itinerary.model.Place;
import cl.duoc.itinerary.model.PlaceType;
import cl.duoc.itinerary.repository.ItineraryRepository;
import cl.duoc.itinerary.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final ItineraryRepository itineraryRepository;

    public Place addPlaceToItinerary(Long itineraryId, PlaceDTO placeDTO) {
        log.info("Agregando lugar al itinerario: {}", itineraryId);
        
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
            .orElseThrow(() -> new RuntimeException("Itinerario no encontrado con id: " + itineraryId));
        
        Place place = new Place();
        place.setName(placeDTO.getName());
        place.setPlaceType(placeDTO.getPlaceType());
        place.setDescription(placeDTO.getDescription());
        place.setAddress(placeDTO.getAddress());
        place.setVisitDate(placeDTO.getVisitDate());
        place.setVisitOrder(placeDTO.getVisitOrder());
        place.setEstimatedHours(placeDTO.getEstimatedHours());
        place.setContactPhone(placeDTO.getContactPhone());
        place.setWebsite(placeDTO.getWebsite());
        place.setIsActive(true);
        place.setItinerary(itinerary);
        
        return placeRepository.save(place);
    }

    public Place updatePlace(Long placeId, PlaceDTO placeDTO) {
        log.info("Actualizando lugar con id: {}", placeId);
        
        Place place = getPlaceById(placeId);
        
        place.setName(placeDTO.getName());
        place.setPlaceType(placeDTO.getPlaceType());
        place.setDescription(placeDTO.getDescription());
        place.setAddress(placeDTO.getAddress());
        place.setVisitDate(placeDTO.getVisitDate());
        place.setVisitOrder(placeDTO.getVisitOrder());
        place.setEstimatedHours(placeDTO.getEstimatedHours());
        place.setContactPhone(placeDTO.getContactPhone());
        place.setWebsite(placeDTO.getWebsite());
        
        return placeRepository.save(place);
    }

    public void removePlaceFromItinerary(Long itineraryId, Long placeId) {
        log.info("Eliminando lugar {} del itinerario {}", placeId, itineraryId);
        
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
            .orElseThrow(() -> new RuntimeException("Itinerario no encontrado con id: " + itineraryId));
        
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> new RuntimeException("Lugar no encontrado con id: " + placeId));
        
        if (!place.getItinerary().getId().equals(itineraryId)) {
            throw new RuntimeException("El lugar no pertenece a este itinerario");
        }
        
        place.setItinerary(null);
        itinerary.getPlaces().remove(place);
        placeRepository.delete(place);
    }

    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lugar no encontrado con id: " + id));
    }

    public List<Place> getPlacesByItinerary(Long itineraryId) {
        return placeRepository.findByItineraryId(itineraryId);
    }

    public List<Place> getPlacesByType(PlaceType placeType) {
        return placeRepository.findByPlaceType(placeType);
    }

    public List<Place> getPlacesByItineraryAndType(Long itineraryId, PlaceType placeType) {
        return placeRepository.findByItineraryIdAndPlaceType(itineraryId, placeType);
    }

    public Place reorderPlace(Long placeId, Integer newOrder) {
        Place place = getPlaceById(placeId);
        place.setVisitOrder(newOrder);
        return placeRepository.save(place);
    }
}