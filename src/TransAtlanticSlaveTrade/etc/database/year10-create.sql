

CREATE TABLE year10
(
  id bigint NOT NULL,
  "name" character varying(200),
  CONSTRAINT primary_key_year10 PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
ALTER TABLE year10 OWNER TO tast;
GRANT ALL ON TABLE year10 TO tast;


insert into year10(id, name) values( 1, 'years1501-1510');
insert into year10(id, name) values( 2, 'years1511-20');
insert into year10(id, name) values( 3, 'years1521-30');
insert into year10(id, name) values( 4, 'years1531-40');
insert into year10(id, name) values( 5, 'years1541-50');
insert into year10(id, name) values( 6, 'years1551-60');
insert into year10(id, name) values( 7, 'years1561-70');
insert into year10(id, name) values( 8, 'years1571-1580');
insert into year10(id, name) values( 9, 'years1581-1590');
insert into year10(id, name) values( 10, 'years1591-1600');
insert into year10(id, name) values( 11, 'years1601-10');
insert into year10(id, name) values( 12, 'years1611-20');
insert into year10(id, name) values( 13, 'years1621-30');
insert into year10(id, name) values( 14, 'years1631-40');
insert into year10(id, name) values( 15, 'years1641-1650');
insert into year10(id, name) values( 16, 'years1651-60');
insert into year10(id, name) values( 17, 'years1661-70');
insert into year10(id, name) values( 18, 'years1671-1680');
insert into year10(id, name) values( 19, 'years1681-90');
insert into year10(id, name) values( 20, 'years1691-1700');
insert into year10(id, name) values( 21, 'years1701-10');
insert into year10(id, name) values( 22, 'years1711-20');
insert into year10(id, name) values( 23, 'years1721-30');
insert into year10(id, name) values( 24, 'years1731-40');
insert into year10(id, name) values( 25, 'years1741-50');
insert into year10(id, name) values( 26, 'years1751-60');
insert into year10(id, name) values( 27, 'years1761-70');
insert into year10(id, name) values( 28, 'years1771-1780');
insert into year10(id, name) values( 29, 'years1781-91');
insert into year10(id, name) values( 30, 'years1791-1800');
insert into year10(id, name) values( 31, 'years1801-10');
insert into year10(id, name) values( 32, 'years1811-20');
insert into year10(id, name) values( 33, 'years1821-30');
insert into year10(id, name) values( 34, 'years1831-40');
insert into year10(id, name) values( 35, 'years1841-50');
insert into year10(id, name) values( 36, 'years1851-60');
insert into year10(id, name) values( 37, 'years1861-70');


