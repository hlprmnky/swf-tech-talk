-- WIDGCO parts and widgets database
-- This fill will create a database schema sufficent
-- to run the SWF 'datastore' activities defined
-- in this example. 
-- Run it against a Postgresql RDS instance
-- and then provide that instance in datastoreConfig.yml

CREATE TABLE IF NOT EXISTS suppliers (
  supplier_no SERIAL PRIMARY KEY,
  name text, 
  address text
);

CREATE TABLE IF NOT EXISTS parts (
  part_no SERIAL PRIMARY KEY,
  name text,
  supplier_no integer REFERENCES suppliers,
  on_hand_qty integer
);

CREATE TABLE IF NOT EXISTS widgets ( 
  widget_no SERIAL PRIMARY KEY,
  name text
);

CREATE TABLE IF NOT EXISTS widget_parts (
  widget_parts_no SERIAL PRIMARY KEY,
  widget_no integer REFERENCES widgets,
  part_no integer REFERENCES parts,
  required_qty integer NOT NULL
);


INSERT INTO suppliers (name, address) VALUES ('Thing1 Maker', 'Canada');
INSERT INTO suppliers (name, address) VALUES ('Thing2 Maker', 'USA');
INSERT INTO suppliers (name, address) VALUES ('Thing3 Maker', 'Ireland');

INSERT INTO parts (name, supplier_no) VALUES ('Thing1', (select supplier_no from suppliers where name = 'Thing1 Maker'));
INSERT INTO parts (name, supplier_no) VALUES ('Thing2', (select supplier_no from suppliers where name = 'Thing 2Maker'));
INSERT INTO parts (name, supplier_no) VALUES ('Thing3', (select supplier_no from suppliers where name = 'Thing3 Maker'));

INSERT INTO widgets (name) VALUES ('Valid Widget');
INSERT INTO widgets (name) VALUES ('Other Valid Widget');

INSERT INTO widget_parts (widget_no, part_no, required_qty) VALUES (1, 1, 2);
INSERT INTO widget_parts (widget_no, part_no, required_qty) VALUES (1, 2, 4);
INSERT INTO widget_parts (widget_no, part_no, required_qty) VALUES (1, 3, 1);
INSERT INTO widget_parts (widget_no, part_no, required_qty) VALUES (2, 2, 3);
INSERT INTO widget_parts (widget_no, part_no, required_qty) VALUES (2, 3, 2);
