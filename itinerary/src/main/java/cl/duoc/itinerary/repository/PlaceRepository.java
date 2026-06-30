package cl.duoc.itinerary.repository;

import cl.duoc.itinerary.model.Place;
import cl.duoc.itinerary.model.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    // Buscar lugares por itinerario
    List<Place> findByItineraryId(Long itineraryId);

    // Buscar lugares por itinerario y tipo
    List<Place> findByItineraryIdAndPlaceType(Long itineraryId, PlaceType placeType);

    // Buscar lugares por tipo
    List<Place> findByPlaceType(PlaceType placeType);
}