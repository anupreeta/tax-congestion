CREATE TABLE IF NOT EXISTS city (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS city_holidays (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  date timestamp NOT NULL,
  city_id bigint NOT NULL,
  CONSTRAINT fk_city
  FOREIGN KEY (city_id)
  REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS city_holiday_months (
  city_id bigint NOT NULL PRIMARY KEY,
  is_january smallint NOT NULL DEFAULT '0',
  is_february smallint NOT NULL DEFAULT '0',
  is_march smallint NOT NULL DEFAULT '0',
  is_april smallint NOT NULL DEFAULT '0',
  is_may smallint NOT NULL DEFAULT '0',
  is_june smallint NOT NULL DEFAULT '0',
  is_july smallint NOT NULL DEFAULT '0',
  is_august smallint NOT NULL DEFAULT '0',
  is_september smallint NOT NULL DEFAULT '0',
  is_october smallint NOT NULL DEFAULT '0',
  is_november smallint NOT NULL DEFAULT '0',
  is_december smallint NOT NULL DEFAULT '0',
  CONSTRAINT fk_city
  FOREIGN KEY (city_id)
  REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS city_tax_rules (
  max_tax_per_day int NOT NULL DEFAULT '60',
  number_of_tax_free_days_after_holiday int NOT NULL DEFAULT '0',
  number_of_tax_free_days_before_holiday int NOT NULL DEFAULT '0',
  single_charge_period_mins int NOT NULL DEFAULT '0',
  city_entity_id bigint NOT NULL PRIMARY KEY,
  CONSTRAINT fk_city_entity
  FOREIGN KEY (city_entity_id)
  REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS city_tax_charges (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  city_id BIGINT NOT NULL,
  charge decimal(19,2) NOT NULL,
  start_time time NOT NULL,
  end_time time NOT NULL,
  CONSTRAINT fk_city
  FOREIGN KEY (city_id)
  REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS vehicle (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS city_tax_days (
  city_id bigint NOT NULL PRIMARY KEY,
  is_monday smallint NOT NULL DEFAULT '1',
  is_tuesday smallint NOT NULL DEFAULT '1',
  is_wednesday smallint NOT NULL DEFAULT '1',
  is_thursday smallint NOT NULL DEFAULT '1',
  is_friday smallint NOT NULL DEFAULT '1',
  is_saturday smallint NOT NULL DEFAULT '0',
  is_sunday smallint NOT NULL DEFAULT '0',
  CONSTRAINT fk_city
  FOREIGN KEY (city_id)
  REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS city_vehicle (
  city_id bigint NOT NULL,
  vehicle_id bigint NOT NULL,
  PRIMARY KEY (city_id,vehicle_id),
  CONSTRAINT fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (id),
  CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES city (id)
);

INSERT INTO city (name) VALUES ('Gothenburg');

INSERT INTO city_holidays(date, city_id) VALUES ('2013-01-01 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-03-28 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-03-29 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-04-01 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-05-01 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-05-08 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-05-09 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-06-06 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-06-21 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-11-01 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-12-24 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-12-25 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-12-26 00:00:00.000000',1);
INSERT INTO city_holidays(date, city_id) VALUES ('2013-12-31 00:00:00.000000',1);

INSERT INTO city_holiday_months VALUES (1,0,0,0,0,0,0,1,0,0,0,0,0);

INSERT INTO city_tax_rules VALUES (60,0,1,60,1);
INSERT INTO city_tax_charges (city_id, charge, start_time, end_time) VALUES (1, 8.00,'06:00:00','06:29:59'),
(1, 13.00,'06:30:00','06:59:59'),
(1, 18.00,'07:00:00','07:59:59'),
(1, 13.00,'08:00:00','08:29:59'),
(1, 8.00,'08:30:00','14:59:59'),
(1, 13.00,'15:00:00','15:29:59'),
(1, 18.00,'15:30:00','16:59:59'),
(1, 13.00,'17:00:00','17:59:59'),
(1, 8.00,'18:00:00','18:29:59'),
(1, 0.00,'18:30:00','23:59:59'),
(1, 0.00,'00:00:00','05:59:59');

INSERT INTO vehicle (name) VALUES
('Emergency'),
('Bus'),
('Diplomat'),
('Motorcycle'),
('Military'),
('Foreign'),
('Car'),
('Motorbike');

INSERT INTO city_tax_days VALUES(1, 1, 1, 1, 1, 1, 0, 0);

INSERT INTO city_vehicle VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
COMMIT;