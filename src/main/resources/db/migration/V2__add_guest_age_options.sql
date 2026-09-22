-- ==========================================
-- V2 - Guest Age Options
-- ==========================================

-- Event decides whether child information
-- should be requested for guests.

ALTER TABLE events
ADD COLUMN ask_child BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE events
ADD COLUMN ask_under_three BOOLEAN NOT NULL DEFAULT FALSE;


-- Guest age information.
--
-- child = false, under_three = false
-- -> Adult / 10 years old or older
--
-- child = true, under_three = false
-- -> Child between 3 and 9 years old
--
-- child = true, under_three = true
-- -> Child under 3 years old

ALTER TABLE guests
ADD COLUMN child BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE guests
ADD COLUMN under_three BOOLEAN NOT NULL DEFAULT FALSE;