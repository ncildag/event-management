-- ==========================================
-- V3 - Create Contact Message Table
-- ==========================================

CREATE TABLE IF NOT EXISTS t_contact_message
(
    id BIGSERIAL PRIMARY KEY,

    date_time TIMESTAMP NOT NULL,

    email VARCHAR(50) NOT NULL,

    message VARCHAR(1000) NOT NULL,

    name VARCHAR(50) NOT NULL,

    subject VARCHAR(50) NOT NULL
);