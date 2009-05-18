-- Function: publish(character varying)

-- DROP FUNCTION publish(character varying);

CREATE OR REPLACE FUNCTION publish(revisionname character varying)
  RETURNS boolean AS
$BODY$
declare
	revID integer;
begin
	revID = nextval('revisions_sequence');
	insert into revisions (id, name) values (revID, revisionName);
	update voyages set revision=revID where revision = -1;
	
        execute 'insert into voyages (iid, revision, voyageid,shipname,captaina,captainb,captainc,datedep,plac1tra,plac2tra,plac3tra,npafttra,sla1port,tslavesd,slaarriv,slas32,adpsale1,slas36,adpsale2,slas39,portret,fate,
sourcea,sourceb,sourcec,sourced,sourcee,sourcef,sourceg,sourceh,sourcei,sourcej,sourcek,sourcel,sourcem,sourcen,sourceo,sourcep,sourceq,sourcer,slintend,tonnage,crewdied,ncar13,ncar15,ncar17,guns,crew1,rig,
placcons,yrreg,crew3,resistance,ownera,ownerb,ownerc,ownerd,ownere,ownerf,ownerg,ownerh,owneri,ownerj,ownerk,ownerl,ownerm,ownern,ownero,ownerp,natinimp,retrnreg,yearam,tonmod,vymrtimp,regem1,regem2,regem3,
majbyimp,regdis1,regdis2,regdis3,fate2,fate3,fate4,mjselimp,vymrtrat,slaximp,slamimp,voy2imp,voy1imp,malrat7,chilrat7,womrat7,menrat7,girlrat7,boyrat7,jamcaspr,mjbyptimp,cd,yrcons,placreg,ptdepimp,mjslptimp,
deptregimp,datebuy,datedepam,dateend,dateland1,dateland2,dateland3,dateleftafr,e_majbyimp,e_mjselimp,e_natinimp,portdep,embport,arrport,tontype,sladamer,saild1,saild2,saild3,saild4,saild5,embport2,voyage,
child2,child3,crew4,crew5,adult1,child1,female1,male1,men1,women1,boy1,girl1,female2,male2,men2,women2,boy2,girl2,female3,male3,men3,women3,boy3,girl3,child4,female4,male4,men4,women4,boy4,girl4,child6,female6,
male6,men6,women6,boy6,girl6,crew2,infantm3,infantf3,sladied1,sladied2,sladied3,sladied4,sladied5,sladied6,adult3,insurrec,evgreen,child5,female5,male5,men5,women5,boy5,girl5,arrport2,infant3,infant1,adult5,
adult2,adult4,infant4,crew,suggestion,nppretra,tslavesp,sladvoy,npprior,"national",slinten2,ndesert,sladafri
) 
select nextval(''hibernate_sequence''), -1,voyageid,shipname,captaina,captainb,captainc,datedep,plac1tra,plac2tra,plac3tra,npafttra,sla1port,tslavesd,slaarriv,slas32,adpsale1,slas36,adpsale2,slas39,portret,fate,
sourcea,sourceb,sourcec,sourced,sourcee,sourcef,sourceg,sourceh,sourcei,sourcej,sourcek,sourcel,sourcem,sourcen,sourceo,sourcep,sourceq,sourcer,slintend,tonnage,crewdied,ncar13,ncar15,ncar17,guns,crew1,rig,
placcons,yrreg,crew3,resistance,ownera,ownerb,ownerc,ownerd,ownere,ownerf,ownerg,ownerh,owneri,ownerj,ownerk,ownerl,ownerm,ownern,ownero,ownerp,natinimp,retrnreg,yearam,tonmod,vymrtimp,regem1,regem2,regem3,
majbyimp,regdis1,regdis2,regdis3,fate2,fate3,fate4,mjselimp,vymrtrat,slaximp,slamimp,voy2imp,voy1imp,malrat7,chilrat7,womrat7,menrat7,girlrat7,boyrat7,jamcaspr,mjbyptimp,cd,yrcons,placreg,ptdepimp,mjslptimp,
deptregimp,datebuy,datedepam,dateend,dateland1,dateland2,dateland3,dateleftafr,e_majbyimp,e_mjselimp,e_natinimp,portdep,embport,arrport,tontype,sladamer,saild1,saild2,saild3,saild4,saild5,embport2,voyage,
child2,child3,crew4,crew5,adult1,child1,female1,male1,men1,women1,boy1,girl1,female2,male2,men2,women2,boy2,girl2,female3,male3,men3,women3,boy3,girl3,child4,female4,male4,men4,women4,boy4,girl4,child6,female6,
male6,men6,women6,boy6,girl6,crew2,infantm3,infantf3,sladied1,sladied2,sladied3,sladied4,sladied5,sladied6,adult3,insurrec,evgreen,child5,female5,male5,men5,women5,boy5,girl5,arrport2,infant3,infant1,adult5,
adult2,adult4,infant4,crew,suggestion,nppretra,tslavesp,sladvoy,npprior,"national",slinten2,ndesert,sladafri
from voyages where revision=' || revID;

	return true;
end$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publish(character varying) OWNER TO tast;
