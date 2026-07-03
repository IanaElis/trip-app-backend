CREATE INDEX idx_place_name_city ON places(name, city);

ALTER TABLE airlines ADD CONSTRAINT uk_airline_iata UNIQUE (iata_code);

DROP INDEX IF EXISTS idx_airline_iata;