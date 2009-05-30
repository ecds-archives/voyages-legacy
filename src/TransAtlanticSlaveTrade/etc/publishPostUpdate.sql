-- Function: publishpostupdate()

-- DROP FUNCTION publishpostupdate();

CREATE OR REPLACE FUNCTION publishpostupdate()
  RETURNS boolean AS
$BODY$
BEGIN
        RAISE NOTICE '----------START INDEX CREATE----------';
        CREATE INDEX voyages_full_text_index_captains ON voyages USING gin (captains_index);
CREATE INDEX voyages_full_text_index_owners ON voyages USING gin (owners_index);
CREATE INDEX voyages_full_text_index_shipname ON voyages USING gin (shipname_index);
CREATE INDEX voyages_full_text_index_sources ON voyages USING gin (sources_index);
CREATE INDEX voyages_index_adpsale1 ON voyages USING btree (adpsale1);
CREATE INDEX voyages_index_adpsale1_area ON voyages USING btree (adpsale1_area);
CREATE INDEX voyages_index_adpsale1_region ON voyages USING btree (adpsale1_region);
CREATE INDEX voyages_index_adpsale2 ON voyages USING btree (adpsale2);
CREATE INDEX voyages_index_adpsale2_area ON voyages USING btree (adpsale2_area);
CREATE INDEX voyages_index_adpsale2_region ON voyages USING btree (adpsale2_region);
CREATE INDEX voyages_index_arrport2_area ON voyages USING btree (arrport2_area);
CREATE INDEX voyages_index_arrport2_region ON voyages USING btree (arrport2_region);
CREATE INDEX voyages_index_arrport_area ON voyages USING btree (arrport_area);
CREATE INDEX voyages_index_arrport_region ON voyages USING btree (arrport_region);
CREATE INDEX voyages_index_boyrat7 ON voyages USING btree (boyrat7);
CREATE INDEX voyages_index_captaina ON voyages USING btree (captaina);
CREATE INDEX voyages_index_chilrat7 ON voyages USING btree (chilrat7);
CREATE INDEX voyages_index_crew1 ON voyages USING btree (crew1);
CREATE INDEX voyages_index_crew3 ON voyages USING btree (crew3);
CREATE INDEX voyages_index_crewdied ON voyages USING btree (crewdied);
CREATE INDEX voyages_index_datebuy ON voyages USING btree (datebuy);
CREATE INDEX voyages_index_datedep ON voyages USING btree (datedep);
CREATE INDEX voyages_index_datedepam ON voyages USING btree (datedepam);
CREATE INDEX voyages_index_dateend ON voyages USING btree (dateend);
CREATE INDEX voyages_index_dateland1 ON voyages USING btree (dateland1);
CREATE INDEX voyages_index_dateland2 ON voyages USING btree (dateland2);
CREATE INDEX voyages_index_dateland3 ON voyages USING btree (dateland3);
CREATE INDEX voyages_index_dateleftafr ON voyages USING btree (dateleftafr);
CREATE INDEX voyages_index_embport2_area ON voyages USING btree (embport2_area);
CREATE INDEX voyages_index_embport2_region ON voyages USING btree (embport2_region);
CREATE INDEX voyages_index_embport_area ON voyages USING btree (embport_area);
CREATE INDEX voyages_index_embport_region ON voyages USING btree (embport_region);
CREATE INDEX voyages_index_evgreen ON voyages USING btree (evgreen);
CREATE INDEX voyages_index_fate ON voyages USING btree (fate);
CREATE INDEX voyages_index_fate2 ON voyages USING btree (fate2);
CREATE INDEX voyages_index_fate3 ON voyages USING btree (fate3);
CREATE INDEX voyages_index_fate4 ON voyages USING btree (fate4);
CREATE INDEX voyages_index_girlrat7 ON voyages USING btree (girlrat7);
CREATE INDEX voyages_index_guns ON voyages USING btree (guns);
CREATE UNIQUE INDEX voyages_index_iid ON voyages USING btree (iid);
CREATE INDEX voyages_index_jamcaspr ON voyages USING btree (jamcaspr);
CREATE INDEX voyages_index_malrat7 ON voyages USING btree (malrat7);
CREATE INDEX voyages_index_menrat7 ON voyages USING btree (menrat7);
CREATE INDEX voyages_index_mjbyptimp ON voyages USING btree (mjbyptimp);
CREATE INDEX voyages_index_mjbyptimp_area ON voyages USING btree (mjbyptimp_area);
CREATE INDEX voyages_index_mjbyptimp_region ON voyages USING btree (mjbyptimp_region);
CREATE INDEX voyages_index_mjslptimp ON voyages USING btree (mjslptimp);
CREATE INDEX voyages_index_mjslptimp_area ON voyages USING btree (mjslptimp_area);
CREATE INDEX voyages_index_mjslptimp_region ON voyages USING btree (mjslptimp_region);
CREATE INDEX voyages_index_natinimp ON voyages USING btree (natinimp);
CREATE INDEX voyages_index_national ON voyages USING btree ("national");
CREATE INDEX voyages_index_ncar13 ON voyages USING btree (ncar13);
CREATE INDEX voyages_index_ncar15 ON voyages USING btree (ncar15);
CREATE INDEX voyages_index_ncar17 ON voyages USING btree (ncar17);
CREATE INDEX voyages_index_npafttra ON voyages USING btree (npafttra);
CREATE INDEX voyages_index_npafttra_area ON voyages USING btree (npafttra_area);
CREATE INDEX voyages_index_npafttra_region ON voyages USING btree (npafttra_region);
CREATE INDEX voyages_index_ownera ON voyages USING btree (ownera);
CREATE INDEX voyages_index_plac1tra ON voyages USING btree (plac1tra);
CREATE INDEX voyages_index_plac1tra_area ON voyages USING btree (plac1tra_area);
CREATE INDEX voyages_index_plac1tra_region ON voyages USING btree (plac1tra_region);
CREATE INDEX voyages_index_plac2tra ON voyages USING btree (plac2tra);
CREATE INDEX voyages_index_plac2tra_area ON voyages USING btree (plac2tra_area);
CREATE INDEX voyages_index_plac2tra_region ON voyages USING btree (plac2tra_region);
CREATE INDEX voyages_index_plac3tra ON voyages USING btree (plac3tra);
CREATE INDEX voyages_index_plac3tra_area ON voyages USING btree (plac3tra_area);
CREATE INDEX voyages_index_plac3tra_region ON voyages USING btree (plac3tra_region);
CREATE INDEX voyages_index_placcons ON voyages USING btree (placcons);
CREATE INDEX voyages_index_placcons_area ON voyages USING btree (placcons_area);
CREATE INDEX voyages_index_placcons_region ON voyages USING btree (placcons_region);
CREATE INDEX voyages_index_placreg ON voyages USING btree (placreg);
CREATE INDEX voyages_index_placreg_area ON voyages USING btree (placreg_area);
CREATE INDEX voyages_index_placreg_region ON voyages USING btree (placreg_region);
CREATE INDEX voyages_index_portdep_area ON voyages USING btree (portdep_area);
CREATE INDEX voyages_index_portdep_region ON voyages USING btree (portdep_region);
CREATE INDEX voyages_index_portret ON voyages USING btree (portret);
CREATE INDEX voyages_index_portret_area ON voyages USING btree (portret_area);
CREATE INDEX voyages_index_portret_region ON voyages USING btree (portret_region);
CREATE INDEX voyages_index_ptdepimp ON voyages USING btree (ptdepimp);
CREATE INDEX voyages_index_ptdepimp_area ON voyages USING btree (ptdepimp_area);
CREATE INDEX voyages_index_ptdepimp_region ON voyages USING btree (ptdepimp_region);
CREATE INDEX voyages_index_resistance ON voyages USING btree (resistance);
CREATE INDEX voyages_index_revision ON voyages USING btree (revision);
CREATE INDEX voyages_index_rig ON voyages USING btree (rig);
CREATE INDEX voyages_index_shipname ON voyages USING btree (shipname);
CREATE INDEX voyages_index_sla1port ON voyages USING btree (sla1port);
CREATE INDEX voyages_index_sla1port_area ON voyages USING btree (sla1port_area);
CREATE INDEX voyages_index_sla1port_region ON voyages USING btree (sla1port_region);
CREATE INDEX voyages_index_slaarriv ON voyages USING btree (slaarriv);
CREATE INDEX voyages_index_slamimp ON voyages USING btree (slamimp);
CREATE INDEX voyages_index_slas32 ON voyages USING btree (slas32);
CREATE INDEX voyages_index_slas36 ON voyages USING btree (slas36);
CREATE INDEX voyages_index_slas39 ON voyages USING btree (slas39);
CREATE INDEX voyages_index_slaximp ON voyages USING btree (slaximp);
CREATE INDEX voyages_index_slintend ON voyages USING btree (slintend);
CREATE INDEX voyages_index_sourcea ON voyages USING btree (sourcea);
CREATE INDEX voyages_index_tonmod ON voyages USING btree (tonmod);
CREATE INDEX voyages_index_tonnage ON voyages USING btree (tonnage);
CREATE INDEX voyages_index_tslavesd ON voyages USING btree (tslavesd);
CREATE INDEX voyages_index_voy1imp ON voyages USING btree (voy1imp);
CREATE INDEX voyages_index_voy2imp ON voyages USING btree (voy2imp);
CREATE INDEX voyages_index_voyageid ON voyages USING btree (voyageid);
CREATE INDEX voyages_index_vymrtimp ON voyages USING btree (vymrtimp);
CREATE INDEX voyages_index_vymrtrat ON voyages USING btree (vymrtrat);
CREATE INDEX voyages_index_womrat7 ON voyages USING btree (womrat7);
CREATE INDEX voyages_index_yearam ON voyages USING btree (yearam);
CREATE INDEX voyages_index_yrcons ON voyages USING btree (yrcons);
CREATE INDEX voyages_index_yrreg ON voyages USING btree (yrreg);

        RAISE NOTICE '---------END INDEX CREATE----------';

        ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_adpsale1 FOREIGN KEY (adpsale1) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_adpsale2 FOREIGN KEY (adpsale2) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_e_majbyimp FOREIGN KEY (e_majbyimp) REFERENCES estimates_export_regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_e_natinimp FOREIGN KEY (e_natinimp) REFERENCES estimates_nations(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_fate FOREIGN KEY (fate) REFERENCES fates(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_fate2 FOREIGN KEY (fate2) REFERENCES fates_slaves(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_fate3 FOREIGN KEY (fate3) REFERENCES fates_vessel(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_fate4 FOREIGN KEY (fate4) REFERENCES fates_owner(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_majbyimp FOREIGN KEY (majbyimp) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_mjbyptimp FOREIGN KEY (mjbyptimp) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_mjselimp FOREIGN KEY (mjselimp) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_natinimp FOREIGN KEY (natinimp) REFERENCES nations(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_plac1tra FOREIGN KEY (plac1tra) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_plac2tra FOREIGN KEY (plac2tra) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_plac3tra FOREIGN KEY (plac3tra) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_placcons FOREIGN KEY (placcons) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_portdep FOREIGN KEY (portdep) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_portret FOREIGN KEY (portret) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regdis1 FOREIGN KEY (regdis1) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regdis2 FOREIGN KEY (regdis2) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regdis3 FOREIGN KEY (regdis3) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regem1 FOREIGN KEY (regem1) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regem2 FOREIGN KEY (regem2) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_regem3 FOREIGN KEY (regem3) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_resistance FOREIGN KEY (resistance) REFERENCES resistance(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_retrnreg FOREIGN KEY (retrnreg) REFERENCES regions(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_sla1port FOREIGN KEY (sla1port) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_vessel_rig FOREIGN KEY (rig) REFERENCES vessel_rigs(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_voyage_arrport FOREIGN KEY (arrport) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_voyage_arrport2 FOREIGN KEY (arrport2) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_voyage_embport FOREIGN KEY (embport) REFERENCES ports(id);
ALTER TABLE ONLY voyages ADD CONSTRAINT fk_1_voyage_embport2 FOREIGN KEY (embport2) REFERENCES ports(id);

        RAISE NOTICE '---------START CONSTRAINT CREATE----------';



RAISE NOTICE '---------END CONSTRAINT CREATE----------';

        
	RETURN true;
	
	EXCEPTION
	WHEN OTHERS THEN
	    --Do Nothing
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publishpostupdate() OWNER TO tast;
