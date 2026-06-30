package cl.duoc.itinerary.repository;

import cl.duoc.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    // Buscar itinerarios por ID del viajero
    List<Itinerary> findByTravelerId(Long travelerId);

    // Buscar itinerarios activos por ID del viajero
    List<Itinerary> findByTravelerIdAndIsActiveTrue(Long travelerId);
}