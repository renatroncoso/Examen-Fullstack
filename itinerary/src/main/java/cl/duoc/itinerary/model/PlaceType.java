package cl.duoc.itinerary.model;

public enum PlaceType {
    HOTEL("Hotel"),
    TOUR("Tour"),
    SITIO_TURISTICO("Sitio Turístico");

    private final String displayName;

    PlaceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}