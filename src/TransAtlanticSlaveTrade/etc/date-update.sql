select publishpreupdate();

update voyages 
set datedepc=date_part('year', datedep), 
datedepb=date_part('month', datedep), 
datedepa=date_part('day', datedep) 
where datedep is not null;

update voyages 
set d1slatrc=date_part('year', datebuy), 
d1slatrb=date_part('month', datebuy), 
d1slatra=date_part('day', datebuy) 
where datebuy is not null;

update voyages 
set dlslatrc=date_part('year', dateleftAfr), 
dlslatrb=date_part('month', dateleftAfr), 
dlslatra=date_part('day', dateleftAfr) 
where dateleftAfr is not null;

update voyages 
set datarr34=date_part('year', dateland1), 
datarr33=date_part('month', dateland1), 
datarr32=date_part('day', dateland1) 
where dateland1 is not null;

update voyages 
set datarr38=date_part('year', dateland2), 
datarr37=date_part('month', dateland2), 
datarr36=date_part('day', dateland2) 
where dateland2 is not null;

update voyages 
set datarr41=date_part('year', dateland3), 
datarr40=date_part('month', dateland3), 
datarr39=date_part('day', dateland3) 
where dateland3 is not null;

update voyages 
set ddepamc=date_part('year', datedepam), 
ddepamb=date_part('month', datedepam), 
ddepam=date_part('day', datedepam) 
where datedepam is not null;

update voyages 
set datarr45=date_part('year', dateend), 
datarr44=date_part('month', dateend), 
datarr43=date_part('day', dateend) 
where dateend is not null;

select publishpostupdate();