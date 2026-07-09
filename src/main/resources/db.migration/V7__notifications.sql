CREATE TABLE notifications
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id           BIGINT                   NOT NULL,
    trip_id           BIGINT                   NOT NULL,
    item_id           BIGINT                   NOT NULL,
    item_type         VARCHAR(50)              NOT NULL,
    channel_type      VARCHAR(30)              NOT NULL,
    status            VARCHAR(30)              NOT NULL,
    send_at           TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE notification_rules
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_type      VARCHAR(50) NOT NULL,
    offset_minutes INTEGER     NOT NULL,
    enabled        BOOLEAN     NOT NULL DEFAULT TRUE
);

INSERT INTO notification_rules (item_type, offset_minutes, enabled) VALUES
('FLIGHT', 1440, TRUE),
('FLIGHT', 120, TRUE),
('ACCOMMODATION', 1440, TRUE),
('ACTIVITY', 60, TRUE),
('TRANSPORT', 60, TRUE);

ALTER TABLE flights ALTER COLUMN departure_airport_id SET NOT NULL;
ALTER TABLE flights ALTER COLUMN arrival_airport_id SET NOT NULL;