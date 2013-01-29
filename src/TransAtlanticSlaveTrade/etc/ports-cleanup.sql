CREATE OR REPLACE FUNCTION cleanup_ports (IN port_o bigint, IN port_n bigint) RETURNS bigint AS
'
BEGIN
	UPDATE voyages SET placcons = port_n WHERE placcons = port_o;
	UPDATE voyages SET placreg = port_n WHERE placreg = port_o;
	UPDATE voyages SET embport = port_n WHERE embport = port_o;
	UPDATE voyages SET embport2  = port_n WHERE embport2 = port_o;
	UPDATE voyages SET arrport = port_n WHERE arrport = port_o;
	UPDATE voyages SET arrport2 = port_n WHERE arrport2 = port_o;
	UPDATE voyages SET portdep = port_n WHERE portdep = port_o;
	UPDATE voyages SET deptregimp = port_n WHERE deptregimp = port_o;
	UPDATE voyages SET plac1tra = port_n WHERE plac1tra = port_o;
	UPDATE voyages SET regem1 = port_n WHERE regem1 = port_o;
	UPDATE voyages SET plac2tra = port_n WHERE plac2tra = port_o;
	UPDATE voyages SET regem2 = port_n WHERE regem2 = port_o;
	UPDATE voyages SET plac3tra = port_n WHERE plac3tra = port_o;
	UPDATE voyages SET regem3 = port_n WHERE regem3 = port_o;
	UPDATE voyages SET majbuypt = port_n WHERE majbuypt = port_o;
	UPDATE voyages SET npafttra = port_n WHERE npafttra = port_o;
	UPDATE voyages SET sla1port = port_n WHERE sla1port = port_o;
	UPDATE voyages SET regdis1 = port_n WHERE regdis1 = port_o;
	UPDATE voyages SET regdis1 = port_n WHERE regdis1 = port_o;
	UPDATE voyages SET adpsale1 = port_n WHERE adpsale1 = port_o;
	UPDATE voyages SET regdis2 = port_n WHERE regdis2 = port_o;
	UPDATE voyages SET adpsale2 = port_n WHERE adpsale2 = port_o;
	UPDATE voyages SET majselpt = port_n WHERE majselpt = port_o;
	UPDATE voyages SET portret = port_n WHERE portret = port_o;
	UPDATE voyages SET retrnreg = port_n WHERE retrnreg = port_o;
	UPDATE voyages SET ptdepimp = port_n WHERE ptdepimp = port_o;
	UPDATE voyages SET mjbyptimp = port_n WHERE mjbyptimp = port_o;
	UPDATE voyages SET mjslptimp = port_n WHERE mjslptimp = port_o;

	UPDATE slaves SET majselpt = port_n WHERE  majselpt = port_o;
        UPDATE slaves SET majbuypt = port_n WHERE  majbuypt = port_o;

	return 1;
END;
'
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION estimate(int8, "varchar") OWNER TO tast;


  -- New Ports

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(11601, 11600, 'Sardinia', 0, 0, 11601, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31326, 31300, 'Batabanó  ', 0, 0, 31326, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60301 , 60300 , 'Barason', 0, 0, 60301 , 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60336, 60300, 'Young Sestos', 0, 0, 60336, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60337, 60300, 'Little Sestos', 0, 0, 60337, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60338, 60300, 'Kru Settra', 0, 0, 60338, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60339, 60300, 'Cape Palmas', 0, 0, 60339, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60403, 60400, 'Acoda  (Akka or Acquida)', 0, 0, 60403, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60429, 60400, 'Boutry', 0, 0, 60429, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60530, 60500, 'Bristol Island, Nazareth', 0, 0, 60530, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60340, 60300, 'Settra Kru', 0, 0, 60340, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60241, 60200, 'Mano', 6.983333, -11.283333000000001, 60241, 0, 't', 't');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60242, 60200, 'River Kissey', 8.500132, -13.120396, 60242, 0, 't', 't');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(60627, 60600, 'Rio Nazareth', 0, 0, 60525, 0, 'f', 't');



select cleanup_ports(60203, 60202);
select cleanup_ports(60209, 60340);
select cleanup_ports(60216, 60340);
select cleanup_ports(60304, 60429);
select cleanup_ports(60308, 60306);
select cleanup_ports(60320, 60241);
select cleanup_ports(60327, 60242);
select cleanup_ports(60243, 60219);
select cleanup_ports(60525, 60627);



-- Port Name / ID Corrections

UPDATE ports set name = 'Pokesoe (Princes Town)' WHERE id = 60428;
UPDATE ports set name = 'Grand Sestos' WHERE id = 60329;
UPDATE ports set name = 'Rock Sestos' WHERE id = 60330;
UPDATE ports set name = 'Côte de Malaguette (runs through to Cape Palmas on Windward Coast)' WHERE id = 60205;
UPDATE ports set name = 'Bance Island (Ben''s Island)' WHERE id = 60202;
UPDATE ports set name = 'Cape Mount (Cape Grand Mount)' WHERE id = 60306;
UPDATE ports set name = 'Sugary (Siekere)' WHERE id = 60219;


-- Deleted Ports
DELETE from ports where id in(60203, 60216, 60304, 60308, 60423, 60320, 60327, 60525);

commit:

DROP FUNCTION cleanup_ports(bigint, bigint);