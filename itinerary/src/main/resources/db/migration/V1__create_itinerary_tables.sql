-- =====================================================
-- V1__create_itinerary_tables.sql
-- Creación de tablas para el microservicio de Itinerario
-- =====================================================

-- Eliminar tablas si existen (para desarrollo)
DROP TABLE IF EXISTS places;
DROP TABLE IF EXISTS itineraries;

-- =====================================================
-- Tabla: itineraries
-- =====================================================
CREATE TABLE IF NOT EXISTS itineraries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    traveler_id BIGINT NOT NULL COMMENT 'ID del viajero (referencia al Traveler Service)',
    name VARCHAR(100) NOT NULL COMMENT 'Nombre del itinerario',
    start_date DATE NOT NULL COMMENT 'Fecha de inicio del viaje',
    end_date DATE NOT NULL COMMENT 'Fecha de fin del viaje',
    description VARCHAR(500) COMMENT 'Descripción del itinerario',
    total_days INT COMMENT 'Días totales del viaje',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Estado del itinerario',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabla: places
-- =====================================================
CREATE TABLE IF NOT EXISTS places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'Nombre del lugar',
    place_type ENUM('HOTEL', 'TOUR', 'SITIO_TURISTICO') NOT NULL COMMENT 'Tipo de lugar',
    description VARCHAR(500) COMMENT 'Descripción del lugar',
    address VARCHAR(200) COMMENT 'Dirección del lugar',
    visit_date DATE COMMENT 'Fecha de visita',
    visit_order INT COMMENT 'Orden de visita en el itinerario',
    estimated_hours DECIMAL(5,2) COMMENT 'Horas estimadas de visita',
    contact_phone VARCHAR(50) COMMENT 'Teléfono de contacto',
    website VARCHAR(200) COMMENT 'Sitio web del lugar',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Estado del lugar',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    itinerary_id BIGINT NOT NULL COMMENT 'ID del itinerario al que pertenece',
    
    FOREIGN KEY (itinerary_id) REFERENCES itineraries(id) ON DELETE CASCADE,
    
    INDEX idx_places_itinerary_id (itinerary_id),
    INDEX idx_places_place_type (place_type),
    INDEX idx_places_visit_date (visit_date),
    INDEX idx_places_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Índices adicionales
-- =====================================================
CREATE INDEX idx_itineraries_traveler_id ON itineraries(traveler_id);
CREATE INDEX idx_itineraries_start_date ON itineraries(start_date);
CREATE INDEX idx_itineraries_is_active ON itineraries(is_active);