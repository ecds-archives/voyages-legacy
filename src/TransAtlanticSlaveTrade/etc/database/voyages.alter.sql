ALTER TABLE ONLY voyages 
ADD COLUMN adlt1imp integer,
ADD COLUMN slavmax1 integer,
ADD COLUMN slavmax3 integer,
ADD COLUMN slavmax7 integer,
ADD COLUMN adlt2imp integer,
ADD COLUMN chil2imp integer,
ADD COLUMN male2imp integer,
ADD COLUMN feml2imp integer;

ALTER TABLE voyages ADD COLUMN infant2 integer;
ALTER TABLE voyages ALTER COLUMN infant2 SET STORAGE PLAIN;

ALTER TABLE voyages ADD COLUMN infant5 integer;
ALTER TABLE voyages ALTER COLUMN infant5 SET STORAGE PLAIN;

ALTER TABLE voyages ADD COLUMN infant6 integer;
ALTER TABLE voyages ALTER COLUMN infant6 SET STORAGE PLAIN;