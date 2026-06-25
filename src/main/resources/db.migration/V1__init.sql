CREATE TABLE airlines
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      VARCHAR(200) NOT NULL,
    iata_code VARCHAR(10)
);

CREATE INDEX idx_airline_iata ON airlines(iata_code);

CREATE TABLE companies(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(30) NOT NULL
);

CREATE INDEX idx_company_type ON companies(type);


CREATE TABLE places
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    google_place_id VARCHAR(255) UNIQUE,
    name            VARCHAR(200),
    address         VARCHAR(255),
    city            VARCHAR(100),
    country         VARCHAR(100),
    latitude        DOUBLE PRECISION,
    longitude       DOUBLE PRECISION,
    timezone_id     VARCHAR(100)
);

CREATE INDEX idx_place_google_id ON places(google_place_id);

CREATE TABLE airports
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    place_id  BIGINT NOT NULL REFERENCES places (id) on DELETE CASCADE,
    iata_code VARCHAR(10),
    icao_code VARCHAR(10)
);

CREATE INDEX idx_airport_iata ON airports(iata_code);
CREATE INDEX idx_airport_icao ON airports(icao_code);


CREATE TABLE trips(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id              BIGINT       NOT NULL,
    name                 VARCHAR(200) NOT NULL,
    description          TEXT,
    destination_place_id BIGINT NOT NULL REFERENCES places(id),
    start_date           TIMESTAMPTZ  NOT NULL,
    end_date             TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_trips_user_id ON trips(user_id);
CREATE INDEX idx_trips_user_id_start_date ON trips(user_id, start_date);


CREATE TABLE base_itinerary_items(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    trip_id        BIGINT      NOT NULL REFERENCES trips (id) ON DELETE CASCADE,
    start_datetime TIMESTAMPTZ NOT NULL,
    end_datetime   TIMESTAMPTZ NOT NULL,
    notes          TEXT
);

CREATE INDEX idx_itinerary_trip_start ON base_itinerary_items(trip_id, start_datetime);


CREATE TABLE activities(
    id          BIGINT PRIMARY KEY REFERENCES base_itinerary_items (id) ON DELETE CASCADE,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    location_id BIGINT       REFERENCES places (id)
);


CREATE TABLE accommodations(
    id                 BIGINT PRIMARY KEY REFERENCES base_itinerary_items (id) ON DELETE CASCADE,
    reservation_number VARCHAR(100),
    location_id        BIGINT REFERENCES places (id)
);


CREATE TABLE transports(
    id                    BIGINT PRIMARY KEY REFERENCES base_itinerary_items (id) ON DELETE CASCADE,
    confirmation_number   VARCHAR(100),
    company_id            BIGINT REFERENCES companies (id),
    transport_type        VARCHAR(30) NOT NULL,
    departure_location_id BIGINT      NOT NULL REFERENCES places (id),
    arrival_location_id   BIGINT      NOT NULL REFERENCES places (id),
    transport_identifier  VARCHAR(100)
);


CREATE TABLE flights
(
    id                   BIGINT PRIMARY KEY REFERENCES base_itinerary_items (id) ON DELETE CASCADE,
    confirmation_number  VARCHAR(100),
    airline_id           BIGINT REFERENCES airlines (id),
    departure_airport_id BIGINT REFERENCES airports (id),
    arrival_airport_id   BIGINT REFERENCES airports (id),
    flight_number        VARCHAR(20)
);
