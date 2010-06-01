
CREATE TABLE year25
(
  id bigint NOT NULL,
  "name" character varying(200),
  CONSTRAINT primary_key_year25 PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
ALTER TABLE year25 OWNER TO tast;
GRANT ALL ON TABLE year25 TO tast;

insert into year25(id, name) values(1, 'years 1501-1525');
insert into year25(id, name) values(2, 'years 1526-1550');
insert into year25(id, name) values(3, 'years 1551-75');
insert into year25(id, name) values(4, 'years 1576-1600');
insert into year25(id, name) values(5, 'years 1601-25');
insert into year25(id, name) values(6, 'years 1626-50');
insert into year25(id, name) values(7, 'years 1651-75');
insert into year25(id, name) values(8, 'years 1676-1700');
insert into year25(id, name) values(9, 'years 1701-25');
insert into year25(id, name) values(10, 'years 1726-50');
insert into year25(id, name) values(11, 'years 1751-75');
insert into year25(id, name) values(12, 'years 1776-1800');
insert into year25(id, name) values(13, 'years 1801-25');
insert into year25(id, name) values(14, 'years 1826-50');
insert into year25(id, name) values(15, 'years 1851-75');