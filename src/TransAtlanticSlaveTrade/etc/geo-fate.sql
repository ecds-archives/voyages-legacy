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


--Updated Regions
UPDATE regions SET name= 'Hispaniola' WHERE id=  31100;
UPDATE regions SET name= 'Other Spanish Caribbean' WHERE id=  31400;
UPDATE regions SET name= 'Spanish Circum-Caribbean' WHERE id=  41200;

--Updated ports
UPDATE ports SET name= 'Hispaniola, unspecified' WHERE id=  31199;
UPDATE ports SET name= 'Spanish Caribbean, unspecified' WHERE id=  31499;
UPDATE ports SET name= 'Spanish Circum-Caribbean,unspecified' WHERE id=  41299;
UPDATE ports SET name= 'Veracruz' WHERE id=  41203;
UPDATE ports SET name= 'La Guaira' WHERE id=  41214;

-- New Ports
INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31106, 31100, 'Santo Domingo (port)', 0, 0, 31106, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31107, 31100, 'Ocoa', 0, 0, 31107, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31108, 31100, 'Nizao', 0, 0, 31108, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31109, 31100, 'Puerto de Plata', 0, 0, 31109, 0, 'f', 'f');


INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31110, 31100, 'Isla Saona', 0, 0, 31110, 0, 'f', 'f');


INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31327, 31300, 'Baracoa', 0, 0, 31327, 0, 'f', 'f');

INSERT INTO ports
(id, region_id, name, latitude, longitude, order_num, show_at_zoom, show_on_main_map, show_on_voyage_map)
VALUES(31328, 31300, 'Punta Macurijes', 0, 0, 31328, 0, 'f', 'f');


-- change codes in voyages
select cleanup_ports(31105, 31199);


-- Deleted Ports
DELETE from ports where id in (31105);


-- Update estimaates codes
UPDATE estimates_import_regions SET name ='Spanish Circum-Caribbean' where id=703;

-- New Fates
INSERT INTO fates
VALUES(213, 'Captured by the Dominican Republic');


DROP FUNCTION cleanup_ports(bigint, bigint);

commit;
