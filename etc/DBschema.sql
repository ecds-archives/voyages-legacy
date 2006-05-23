-- Role: "tasuser"
-- DROP ROLE tasuser;
CREATE ROLE tasuser LOGIN
  NOSUPERUSER NOINHERIT CREATEDB NOCREATEROLE;


-- Database: tasdb
-- DROP DATABASE tasdb;
CREATE DATABASE tasdb
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default;



-- Sequence: hibernate_sequence
-- DROP SEQUENCE hibernate_sequence;
CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 184
  CACHE 1;
ALTER TABLE hibernate_sequence OWNER TO tasuser;


-- Table: slaves
-- DROP TABLE slaves;
CREATE TABLE slaves
(
  sid int8 NOT NULL,
  iid int8 NOT NULL,
  slave_name text,
  CONSTRAINT pk_slaves PRIMARY KEY (iid)
) 
WITHOUT OIDS;
ALTER TABLE slaves OWNER TO tasuser;


-- Table: voyages
-- DROP TABLE voyages;
CREATE TABLE voyages
(
  vid int8 NOT NULL,
  ship_name text,
  iid int8 NOT NULL,
  CONSTRAINT pk_voyage PRIMARY KEY (iid)
) 
WITHOUT OIDS;
ALTER TABLE voyages OWNER TO tasuser;


-- Table: voyages_index
-- DROP TABLE voyages_index;
CREATE TABLE voyages_index
(
  vid int8 NOT NULL,
  global_rev_id int8 NOT NULL,
  flag int2 DEFAULT 0,
  revision_date date NOT NULL,
  voyage_index_id int8 NOT NULL,
  r_voyage_iid int8 NOT NULL,
  CONSTRAINT pk_voyage_index PRIMARY KEY (voyage_index_id),
  CONSTRAINT fk_voyage_index_to_voyage FOREIGN KEY (r_voyage_iid)
      REFERENCES voyages (iid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;
ALTER TABLE voyages_index OWNER TO tasuser;


-- Table: voyage_slaves
-- DROP TABLE voyage_slaves;
CREATE TABLE voyage_slaves
(
  r_slave_id int8 NOT NULL,
  r_voyage_index_id int8 NOT NULL,
  CONSTRAINT pk_voyage_slaves PRIMARY KEY (r_slave_id, r_voyage_index_id),
  CONSTRAINT fk_voyage_slave_to_slave FOREIGN KEY (r_slave_id)
      REFERENCES slaves (iid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_voyage_slaves_to_voyage_index FOREIGN KEY (r_voyage_index_id)
      REFERENCES voyages_index (voyage_index_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;
ALTER TABLE voyage_slaves OWNER TO tasuser;
