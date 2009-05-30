-- Function: publishpreupdate()

-- DROP FUNCTION publishpreupdate();

CREATE OR REPLACE FUNCTION publishpreupdate()
  RETURNS boolean AS
$BODY$
BEGIN
        RAISE NOTICE '----------START INDEX DELETE----------';

        DROP INDEX voyages_full_text_index_captains;
DROP INDEX voyages_full_text_index_owners;
DROP INDEX voyages_full_text_index_shipname;
DROP INDEX voyages_full_text_index_sources;
DROP INDEX voyages_index_adpsale1;
DROP INDEX voyages_index_adpsale1_area;
DROP INDEX voyages_index_adpsale1_region;
DROP INDEX voyages_index_adpsale2;
DROP INDEX voyages_index_adpsale2_area;
DROP INDEX voyages_index_adpsale2_region;
DROP INDEX voyages_index_arrport2_area;
DROP INDEX voyages_index_arrport2_region;
DROP INDEX voyages_index_arrport_area;
DROP INDEX voyages_index_arrport_region;
DROP INDEX voyages_index_boyrat7;
DROP INDEX voyages_index_captaina;
DROP INDEX voyages_index_chilrat7;
DROP INDEX voyages_index_crew1;
DROP INDEX voyages_index_crew3;
DROP INDEX voyages_index_crewdied;
DROP INDEX voyages_index_datebuy;
DROP INDEX voyages_index_datedep;
DROP INDEX voyages_index_datedepam;
DROP INDEX voyages_index_dateend;
DROP INDEX voyages_index_dateland1;
DROP INDEX voyages_index_dateland2;
DROP INDEX voyages_index_dateland3;
DROP INDEX voyages_index_dateleftafr;
DROP INDEX voyages_index_embport2_area;
DROP INDEX voyages_index_embport2_region;
DROP INDEX voyages_index_embport_area;
DROP INDEX voyages_index_embport_region;
DROP INDEX voyages_index_evgreen;
DROP INDEX voyages_index_fate;
DROP INDEX voyages_index_fate2;
DROP INDEX voyages_index_fate3;
DROP INDEX voyages_index_fate4;
DROP INDEX voyages_index_girlrat7;
DROP INDEX voyages_index_guns;
DROP INDEX voyages_index_iid;
DROP INDEX voyages_index_jamcaspr;
DROP INDEX voyages_index_malrat7;
DROP INDEX voyages_index_menrat7;
DROP INDEX voyages_index_mjbyptimp;
DROP INDEX voyages_index_mjbyptimp_area;
DROP INDEX voyages_index_mjbyptimp_region;
DROP INDEX voyages_index_mjslptimp;
DROP INDEX voyages_index_mjslptimp_area;
DROP INDEX voyages_index_mjslptimp_region;
DROP INDEX voyages_index_natinimp;
DROP INDEX voyages_index_national;
DROP INDEX voyages_index_ncar13;
DROP INDEX voyages_index_ncar15;
DROP INDEX voyages_index_ncar17;
DROP INDEX voyages_index_npafttra;
DROP INDEX voyages_index_npafttra_area;
DROP INDEX voyages_index_npafttra_region;
DROP INDEX voyages_index_ownera;
DROP INDEX voyages_index_plac1tra;
DROP INDEX voyages_index_plac1tra_area;
DROP INDEX voyages_index_plac1tra_region;
DROP INDEX voyages_index_plac2tra;
DROP INDEX voyages_index_plac2tra_area;
DROP INDEX voyages_index_plac2tra_region;
DROP INDEX voyages_index_plac3tra;
DROP INDEX voyages_index_plac3tra_area;
DROP INDEX voyages_index_plac3tra_region;
DROP INDEX voyages_index_placcons;
DROP INDEX voyages_index_placcons_area;
DROP INDEX voyages_index_placcons_region;
DROP INDEX voyages_index_placreg;
DROP INDEX voyages_index_placreg_area;
DROP INDEX voyages_index_placreg_region;
DROP INDEX voyages_index_portdep_area;
DROP INDEX voyages_index_portdep_region;
DROP INDEX voyages_index_portret;
DROP INDEX voyages_index_portret_area;
DROP INDEX voyages_index_portret_region;
DROP INDEX voyages_index_ptdepimp;
DROP INDEX voyages_index_ptdepimp_area;
DROP INDEX voyages_index_ptdepimp_region;
DROP INDEX voyages_index_resistance;
DROP INDEX voyages_index_revision;
DROP INDEX voyages_index_rig;
DROP INDEX voyages_index_shipname;
DROP INDEX voyages_index_sla1port;
DROP INDEX voyages_index_sla1port_area;
DROP INDEX voyages_index_sla1port_region;
DROP INDEX voyages_index_slaarriv;
DROP INDEX voyages_index_slamimp;
DROP INDEX voyages_index_slas32;
DROP INDEX voyages_index_slas36;
DROP INDEX voyages_index_slas39;
DROP INDEX voyages_index_slaximp;
DROP INDEX voyages_index_slintend;
DROP INDEX voyages_index_sourcea;
DROP INDEX voyages_index_tonmod;
DROP INDEX voyages_index_tonnage;
DROP INDEX voyages_index_tslavesd;
DROP INDEX voyages_index_voy1imp;
DROP INDEX voyages_index_voy2imp;
DROP INDEX voyages_index_voyageid;
DROP INDEX voyages_index_vymrtimp;
DROP INDEX voyages_index_vymrtrat;
DROP INDEX voyages_index_womrat7;
DROP INDEX voyages_index_yearam;
DROP INDEX voyages_index_yrcons;
DROP INDEX voyages_index_yrreg;


	 
        RAISE NOTICE '---------END INDEX DELETE----------';

        RAISE NOTICE '---------START CONSTRAINT DELETE----------';

ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_adpsale1;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_adpsale2;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_e_majbyimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_e_natinimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_fate;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_fate2;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_fate3;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_fate4;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_majbyimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_mjbyptimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_mjselimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_natinimp;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_plac1tra;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_plac2tra;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_plac3tra;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_placcons;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_portdep;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_portret;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regdis1;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regdis2;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regdis3;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regem1;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regem2;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_regem3;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_resistance;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_retrnreg;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_sla1port;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_vessel_rig;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_voyage_arrport;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_voyage_arrport2;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_voyage_embport;
ALTER TABLE ONLY voyages DROP CONSTRAINT fk_1_voyage_embport2;

RAISE NOTICE '---------END CONSTRAINT DELETE----------';

        
	RETURN true;
	
	EXCEPTION
	WHEN OTHERS THEN
	    --Do Nothing
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publishpreupdate() OWNER TO tast;
