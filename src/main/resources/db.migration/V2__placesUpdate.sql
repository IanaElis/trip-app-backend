ALTER TABLE places ALTER COLUMN name SET NOT NULL;
ALTER TABLE places ALTER COLUMN latitude SET NOT NULL;
ALTER TABLE places ALTER COLUMN longitude SET NOT NULL;

ALTER TABLE airports ADD CONSTRAINT uk_airport_iata UNIQUE (iata_code);
ALTER TABLE airports ADD CONSTRAINT uk_airport_icao UNIQUE (icao_code);

DROP INDEX IF EXISTS idx_airport_iata;
DROP INDEX IF EXISTS idx_airport_icao;

