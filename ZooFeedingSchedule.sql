-- =============================================================
--  ZooFeedingSchedule — MySQL Schema (3NF, Table-per-Type)
--  Strategy: Table-per-Type (TPT) for Habitat & Staff hierarchies
--  Tested on MySQL 8.0+
-- =============================================================

use ZooFeedingSchedule;


SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS
  feeding_schedule, food,
  animal,
  staff_manager, staff_keeper, staff,
  habitat_savannah, habitat_ocean, habitat_forest, habitat;
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================
-- 1. HABITAT HIERARCHY  (base + three sub-type tables)
-- =============================================================

CREATE TABLE habitat (
    habitat_id      INT             NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL,
    location        VARCHAR(150)    NOT NULL,
    habitat_type    ENUM('FOREST', 'OCEAN', 'SAVANNAH') NOT NULL,
    capacity        SMALLINT        NOT NULL DEFAULT 10
                        COMMENT 'Max number of animals this habitat can hold',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_habitat PRIMARY KEY (habitat_id),
    CONSTRAINT uq_habitat_name UNIQUE (name)
) ENGINE=InnoDB;

-- Forest-specific attributes
CREATE TABLE habitat_forest (
    habitat_id              INT             NOT NULL,
    dominant_tree_species   VARCHAR(100)    NOT NULL,
    canopy_coverage_pct     DECIMAL(5,2)    NOT NULL DEFAULT 0.00
                                CHECK (canopy_coverage_pct BETWEEN 0 AND 100),

    CONSTRAINT pk_habitat_forest PRIMARY KEY (habitat_id),
    CONSTRAINT fk_forest_habitat
        FOREIGN KEY (habitat_id) REFERENCES habitat (habitat_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Ocean-specific attributes
CREATE TABLE habitat_ocean (
    habitat_id      INT             NOT NULL,
    salinity_ppt    DECIMAL(6,3)    NOT NULL
                        COMMENT 'Salinity in parts per thousand',
    depth_meters    DECIMAL(7,2)    NOT NULL,
    ocean_zone      ENUM('SUNLIGHT', 'TWILIGHT', 'MIDNIGHT') NOT NULL,

    CONSTRAINT pk_habitat_ocean PRIMARY KEY (habitat_id),
    CONSTRAINT fk_ocean_habitat
        FOREIGN KEY (habitat_id) REFERENCES habitat (habitat_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Savannah-specific attributes
CREATE TABLE habitat_savannah (
    habitat_id              INT             NOT NULL,
    grassland_area_sqm      DECIMAL(10,2)   NOT NULL,
    dry_season_months       VARCHAR(50)     NOT NULL
                                COMMENT 'E.g. "June,July,August"',

    CONSTRAINT pk_habitat_savannah PRIMARY KEY (habitat_id),
    CONSTRAINT fk_savannah_habitat
        FOREIGN KEY (habitat_id) REFERENCES habitat (habitat_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


-- =============================================================
-- 2. ANIMAL
-- =============================================================

CREATE TABLE animal (
    animal_id       INT             NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100)    NOT NULL,
    species         VARCHAR(100)    NOT NULL,
    diet_type       ENUM('HERBIVORE', 'CARNIVORE', 'OMNIVORE') NOT NULL,
    weight_kg       DECIMAL(8,2)    NOT NULL CHECK (weight_kg > 0),
    date_of_birth   DATE            NULL,
    habitat_id      INT             NOT NULL,

    CONSTRAINT pk_animal  PRIMARY KEY (animal_id),
    CONSTRAINT fk_animal_habitat
        FOREIGN KEY (habitat_id) REFERENCES habitat (habitat_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


-- =============================================================
-- 3. FOOD
-- =============================================================

CREATE TABLE food (
    food_id             INT             NOT NULL AUTO_INCREMENT,
    name                VARCHAR(100)    NOT NULL,
    food_category       ENUM('MEAT', 'FISH', 'VEGETABLE', 'FRUIT',
                             'GRAIN', 'INSECT', 'SUPPLEMENT') NOT NULL,
    calories_per_kg     DECIMAL(8,2)    NOT NULL CHECK (calories_per_kg >= 0),
    unit_of_measure     VARCHAR(20)     NOT NULL DEFAULT 'kg',

    CONSTRAINT pk_food PRIMARY KEY (food_id),
    CONSTRAINT uq_food_name UNIQUE (name)
) ENGINE=InnoDB;


-- =============================================================
-- 4. STAFF HIERARCHY  (base + keeper + manager sub-type tables)
-- =============================================================

CREATE TABLE staff (
    staff_id        INT             NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(80)     NOT NULL,
    last_name       VARCHAR(80)     NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    phone           VARCHAR(25)     NULL,
    staff_type      ENUM('KEEPER', 'MANAGER') NOT NULL,
    hire_date       DATE            NOT NULL,
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_staff  PRIMARY KEY (staff_id),
    CONSTRAINT uq_staff_email UNIQUE (email)
) ENGINE=InnoDB;

-- Keeper-specific attributes
CREATE TABLE staff_keeper (
    staff_id                INT             NOT NULL,
    certification_level     ENUM('TRAINEE', 'JUNIOR', 'SENIOR', 'LEAD') NOT NULL DEFAULT 'JUNIOR',
    assigned_habitat_id     INT             NOT NULL,

    CONSTRAINT pk_staff_keeper PRIMARY KEY (staff_id),
    CONSTRAINT fk_keeper_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_keeper_habitat
        FOREIGN KEY (assigned_habitat_id) REFERENCES habitat (habitat_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Manager-specific attributes
CREATE TABLE staff_manager (
    staff_id                INT             NOT NULL,
    department              VARCHAR(100)    NOT NULL,
    managed_staff_count     SMALLINT        NOT NULL DEFAULT 0,

    CONSTRAINT pk_staff_manager PRIMARY KEY (staff_id),
    CONSTRAINT fk_manager_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


-- =============================================================
-- 5. FEEDING_SCHEDULE  (associative / junction entity)
-- =============================================================

CREATE TABLE feeding_schedule (
    schedule_id     INT             NOT NULL AUTO_INCREMENT,
    animal_id       INT             NOT NULL,
    staff_id        INT             NOT NULL,
    food_id         INT             NOT NULL,
    feeding_time    TIME            NOT NULL,
    day_of_week     ENUM('MONDAY','TUESDAY','WEDNESDAY',
                         'THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
    quantity_kg     DECIMAL(6,3)    NOT NULL CHECK (quantity_kg > 0),
    notes           TEXT            NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_feeding_schedule PRIMARY KEY (schedule_id),
    CONSTRAINT fk_fs_animal
        FOREIGN KEY (animal_id) REFERENCES animal (animal_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_fs_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_fs_food
        FOREIGN KEY (food_id) REFERENCES food (food_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    -- Prevent duplicate schedule entries per animal per slot
    CONSTRAINT uq_schedule_slot UNIQUE (animal_id, day_of_week, feeding_time)
) ENGINE=InnoDB;

-- Index to speed up daily schedule lookups by keeper
CREATE INDEX idx_fs_staff_day ON feeding_schedule (staff_id, day_of_week);
CREATE INDEX idx_fs_animal    ON feeding_schedule (animal_id);


-- =============================================================
-- SAMPLE DATA
-- =============================================================

-- ---- Habitats ------------------------------------------------
INSERT INTO habitat (name, location, habitat_type, capacity) VALUES
  ('Borneo Rainforest',   'East Wing, Block A', 'FOREST',   8),
  ('Pacific Reef',        'West Wing, Block B', 'OCEAN',   15),
  ('East African Plains', 'South Wing, Block C','SAVANNAH', 12);

INSERT INTO habitat_forest (habitat_id, dominant_tree_species, canopy_coverage_pct)
VALUES (1, 'Dipterocarpus', 85.50);

INSERT INTO habitat_ocean (habitat_id, salinity_ppt, depth_meters, ocean_zone)
VALUES (2, 34.500, 12.00, 'SUNLIGHT');

INSERT INTO habitat_savannah (habitat_id, grassland_area_sqm, dry_season_months)
VALUES (3, 15000.00, 'June,July,August,September');

-- ---- Animals -------------------------------------------------
INSERT INTO animal (name, species, diet_type, weight_kg, date_of_birth, habitat_id) VALUES
  ('Raja',    'Orangutan',      'OMNIVORE',  78.50, '2012-03-15', 1),
  ('Nemo',    'Clownfish',      'OMNIVORE',   0.25, '2020-07-01', 2),
  ('Shira',   'Great White Shark','CARNIVORE',522.00,'2015-11-20', 2),
  ('Simba',   'African Lion',   'CARNIVORE', 190.00, '2018-05-10', 3),
  ('Zara',    'African Elephant','HERBIVORE',4200.00,'2010-01-30', 3);

-- ---- Food ----------------------------------------------------
INSERT INTO food (name, food_category, calories_per_kg, unit_of_measure) VALUES
  ('Tropical Fruit Mix', 'FRUIT',       650.00, 'kg'),
  ('Whole Mackerel',     'FISH',       1500.00, 'kg'),
  ('Beef Haunch',        'MEAT',       2500.00, 'kg'),
  ('Mixed Greens',       'VEGETABLE',   250.00, 'kg'),
  ('Zooplankton Blend',  'SUPPLEMENT',  200.00, 'g');

-- ---- Staff ---------------------------------------------------
INSERT INTO staff (first_name, last_name, email, phone, staff_type, hire_date) VALUES
  ('Amara',  'Diallo',   'a.diallo@zoo.com',   '+1-555-0101', 'KEEPER',  '2019-06-01'),
  ('Tom',    'Reeves',   't.reeves@zoo.com',   '+1-555-0102', 'KEEPER',  '2021-03-15'),
  ('Elena',  'Voss',     'e.voss@zoo.com',     '+1-555-0200', 'MANAGER', '2015-01-10');

INSERT INTO staff_keeper (staff_id, certification_level, assigned_habitat_id) VALUES
  (1, 'SENIOR', 1),   -- Amara → Borneo Rainforest
  (2, 'JUNIOR', 3);   -- Tom   → East African Plains

INSERT INTO staff_manager (staff_id, department, managed_staff_count) VALUES
  (3, 'Animal Care', 2);

-- ---- Feeding Schedule ----------------------------------------
INSERT INTO feeding_schedule (animal_id, staff_id, food_id, feeding_time, day_of_week, quantity_kg, notes) VALUES
  (1, 1, 1, '08:00:00', 'MONDAY',    2.500, 'Supplement with vitamin D drops'),
  (1, 1, 1, '08:00:00', 'WEDNESDAY', 2.500, NULL),
  (1, 1, 1, '08:00:00', 'FRIDAY',    2.500, NULL),
  (2, 2, 5, '09:30:00', 'TUESDAY',   0.050, 'Disperse in tank section B'),
  (3, 2, 2, '11:00:00', 'MONDAY',   15.000, 'Drop from feeding platform'),
  (3, 2, 2, '11:00:00', 'THURSDAY', 15.000, NULL),
  (4, 2, 3, '10:00:00', 'WEDNESDAY', 8.000, 'Enrichment hide inside enclosure'),
  (5, 2, 4, '07:30:00', 'MONDAY',   40.000, 'Split across three feeding stations'),
  (5, 2, 4, '07:30:00', 'FRIDAY',   40.000, NULL);