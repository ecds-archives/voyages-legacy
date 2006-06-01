CREATE ROLE tasuser LOGIN
  ENCRYPTED PASSWORD 'md51542627f8efdf6a76f9555f130b9fef0'
  NOSUPERUSER NOINHERIT NOCREATEDB NOCREATEROLE;

CREATE DATABASE tasdb
  WITH OWNER = tasuser
       ENCODING = 'UTF8'
       TABLESPACE = pg_default;



CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1000
  CACHE 1;


-- Table: dictionary

-- DROP TABLE dictionary;

CREATE TABLE dictionary
(
  oid int8 NOT NULL,
  obj_type int4 NOT NULL,
  name varchar(255) NOT NULL,
  remote_id int8,
  CONSTRAINT pk_dictionary PRIMARY KEY (oid)
) 
WITHOUT OIDS;


-- Index: dict_index_obj_type

-- DROP INDEX dict_index_obj_type;

CREATE INDEX dict_index_obj_type
  ON dictionary
  USING btree
  (obj_type);

-- Index: dict_index_remote_id

-- DROP INDEX dict_index_remote_id;

CREATE INDEX dict_index_remote_id
  ON dictionary
  USING btree
  (remote_id);


-- Table: slaves

-- DROP TABLE slaves;

CREATE TABLE slaves
(
  sid int8 NOT NULL,
  iid int8 NOT NULL,
  slave_name text,
  sexage int8,
  age float8,
  feet float8,
  inch float8,
  country int8,
  name varchar(25),
  unclear int8,
  nameimp varchar(25),
  natimp int8,
  bodypt1 int8,
  placea1 int8,
  placeb1 int8,
  marksize int8,
  marktype int8,
  marknumb int8,
  markdegr int8,
  othrmark int8,
  markspec int8,
  bodypt2 int8,
  placea2 int8,
  placeb2 int8,
  marksiz2 int8,
  marktyp2 int8,
  marknum2 int8,
  markdeg2 int8,
  bodypt3 int8,
  placea3 int8,
  placeb3 int8,
  marksiz3 int8,
  marktyp3 int8,
  marknum3 int8,
  markdeg3 int8,
  bodypt4 int8,
  bodypt5 int8,
  bodypt6 int8,
  placea4 int8,
  placea5 int8,
  placea6 int8,
  placeb4 int8,
  placeb5 int8,
  placeb6 int8,
  marksiz4 int8,
  marksiz5 int8,
  marksiz6 int8,
  marktyp6 int8,
  marktyp5 int8,
  marktyp4 int8,
  marknum4 int8,
  marknum5 int8,
  marknum6 int8,
  markdeg6 int8,
  markdeg5 int8,
  markdeg4 int8,
  majselpt int8,
  source varchar(8),
  majbuypt int8,
  datarr34 int8,
  regem1 int8,
  ageinf int2,
  CONSTRAINT pk_slaves PRIMARY KEY (iid),
  CONSTRAINT bodypt1 FOREIGN KEY (bodypt1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bodypt2 FOREIGN KEY (bodypt2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bodypt3 FOREIGN KEY (bodypt3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bodypt4 FOREIGN KEY (bodypt4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bodypt5 FOREIGN KEY (bodypt5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bodypt6 FOREIGN KEY (bodypt6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT country FOREIGN KEY (country)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdeg2 FOREIGN KEY (markdeg2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdeg3 FOREIGN KEY (markdeg3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdeg4 FOREIGN KEY (markdeg4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdeg5 FOREIGN KEY (markdeg5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdeg6 FOREIGN KEY (markdeg6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markdegr FOREIGN KEY (markdegr)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknum2 FOREIGN KEY (marknum2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknum3 FOREIGN KEY (marknum3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknum4 FOREIGN KEY (marknum4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknum5 FOREIGN KEY (marknum5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknum6 FOREIGN KEY (marknum6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marknumb FOREIGN KEY (marknumb)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksiz2 FOREIGN KEY (marksiz2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksiz3 FOREIGN KEY (marksiz3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksiz4 FOREIGN KEY (marksiz4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksiz5 FOREIGN KEY (marksiz5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksiz6 FOREIGN KEY (marksiz6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marksize FOREIGN KEY (marksize)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT markspec FOREIGN KEY (markspec)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktyp2 FOREIGN KEY (marktyp2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktyp3 FOREIGN KEY (marktyp3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktyp4 FOREIGN KEY (marktyp4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktyp5 FOREIGN KEY (marktyp5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktyp6 FOREIGN KEY (marktyp6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT marktype FOREIGN KEY (marktype)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT natimp FOREIGN KEY (natimp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT othrmark FOREIGN KEY (othrmark)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea1 FOREIGN KEY (placea1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea2 FOREIGN KEY (placea2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea3 FOREIGN KEY (placea3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea4 FOREIGN KEY (placea4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea5 FOREIGN KEY (placea5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placea6 FOREIGN KEY (placea6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb1 FOREIGN KEY (placeb1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb2 FOREIGN KEY (placeb2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb3 FOREIGN KEY (placeb3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb4 FOREIGN KEY (placeb4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb5 FOREIGN KEY (placeb5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placeb6 FOREIGN KEY (placeb6)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sexage FOREIGN KEY (sexage)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;


-- Index: index_iid

-- DROP INDEX index_iid;

CREATE INDEX index_iid
  ON slaves
  USING btree
  (iid);


-- Table: voyages

-- DROP TABLE voyages;

CREATE TABLE voyages
(
  vid int8 NOT NULL,
  ship_name text,
  iid int8 NOT NULL,
  captaina varchar(40),
  captainb varchar(40),
  captainc varchar(40),
  portdep int8,
  datedep date,
  datepl int8,
  embport int8,
  arrport int8,
  nppretra int2,
  plac1tra int8,
  destin int8,
  plac2tra int8,
  plac3tra int8,
  d1slatr date,
  npafttra int8,
  dlslatrb date,
  npprior int2,
  sla1port int8,
  tslavesp int4,
  tslavesd int4,
  sladvoy int4,
  slaarriv int4,
  rrdata31 date,
  slas32 int4,
  adpsale1 int8,
  datarr32 date,
  slas36 int4,
  adpsale2 int8,
  datarr33 date,
  slas39 int4,
  ddepamb date,
  portret int8,
  datarr4 date,
  fate int8,
  sourcea varchar(60),
  sourceb varchar(60),
  sourcec varchar(60),
  sourced varchar(60),
  sourcee varchar(60),
  sourcef varchar(60),
  sourceg varchar(60),
  sourceh varchar(60),
  sourcei varchar(60),
  sourcej varchar(60),
  sourcek varchar(60),
  sourcel varchar(60),
  sourcem varchar(60),
  sourcen varchar(60),
  sourceo varchar(60),
  sourcep varchar(60),
  sourceq varchar(60),
  sourcer varchar(60),
  slintend int4,
  tonnage int4,
  tontype int8,
  "national" int8,
  crewdied int4,
  ncar13 int4,
  ncar15 int4,
  ncar17 int4,
  sladamer int4,
  saild1 int4,
  saild2 int4,
  saild3 int4,
  saild4 int4,
  saild5 int4,
  ndesert int4,
  embport2 int8,
  slinten2 int4,
  guns int4,
  voyage int4,
  crew1 int4,
  rig int8,
  child2 int4,
  child3 int4,
  placcons int8,
  yrreg int4,
  crew3 int4,
  crew4 int4,
  crew5 int4,
  adult1 int4,
  child1 int4,
  female1 int4,
  male1 int4,
  men1 int4,
  women1 int4,
  female2 int4,
  male2 int4,
  men2 int4,
  women2 int4,
  boy2 int4,
  girl2 int4,
  female3 int4,
  male3 int4,
  men3 int4,
  women3 int4,
  boy3 int4,
  girl3 int4,
  female4 int4,
  male4 int4,
  men4 int4,
  women4 int4,
  boy4 int4,
  girl4 int4,
  child4 int4,
  female6 int4,
  male6 int4,
  men6 int4,
  women6 int4,
  boy6 int4,
  girl6 int4,
  child6 int4,
  crew2 int4,
  infantm3 int4,
  infantf3 int4,
  sladafri int4,
  sladied5 int4,
  sladied4 int4,
  sladied2 int4,
  sladied1 int4,
  sladied3 int4,
  sladied6 int4,
  insurrec int8,
  adult3 int4,
  ownera varchar(60),
  ownerb varchar(60),
  ownerc varchar(60),
  ownerd varchar(60),
  ownere varchar(60),
  ownerf varchar(60),
  ownerg varchar(60),
  ownerh varchar(60),
  owneri varchar(60),
  ownerj varchar(60),
  ownerk varchar(60),
  ownerl varchar(60),
  ownerm varchar(60),
  ownern varchar(60),
  ownero varchar(60),
  ownerp varchar(60),
  majselpt int8,
  majbuypt int8,
  natinimp int8,
  deptreg int8,
  retrnreg int8,
  regisreg int8,
  yearaf int4,
  yeardep int4,
  yearam int4,
  year100 int8,
  year5 int8,
  year25 int8,
  tonmod float8,
  vymrtimp int8,
  tslmtimp int8,
  regem1 int8,
  regem2 int8,
  regem3 int8,
  embreg int8,
  embreg2 int8,
  majbuyrg int8,
  majbyimp int8,
  regdis1 int8,
  regdis2 int8,
  regdis3 int8,
  regarrp int8,
  majselrg int8,
  fate2 int8,
  fate3 int8,
  fate4 int8,
  mjselimp int8,
  sla32imp int2,
  sla36imp int2,
  imprat int4,
  regdis11 int8,
  regdis21 int8,
  sla39imp int4,
  ncr15imp int4,
  ncr17imp int4,
  exprat float8,
  male1imp int4,
  feml1imp int4,
  chil1imp int4,
  malrat1 float8,
  chilrat1 float8,
  slavemx1 int4,
  slavema1 int4,
  male3imp int4,
  feml3imp int4,
  chil3imp int4,
  chilrat3 float8,
  malrat3 float8,
  slavemx3 int4,
  slavema3 int4,
  adlt3imp int4,
  vymrtrat float8,
  slaximp int4,
  slamimp int4,
  deptreg1 int8,
  mjselrg1 int8,
  constreg int8,
  voy2imp int4,
  voy1imp int4,
  malrat7 float8,
  chilrat7 float8,
  status int8,
  sourcex varchar(255),
  p int8,
  men8 int4,
  women8 int4,
  boy8 int4,
  girl8 int4,
  behrflag int4,
  price int8,
  uppguine float8,
  voyagimp float8,
  timcoast float8,
  slavpday float8,
  rountrip float8,
  slavpvoy float8,
  slxp1000 float8,
  majbyim3 int8,
  insurre2 float8,
  timslave float8,
  toncateg int8,
  evgreen int8,
  womrat1 float8,
  womrat3 float8,
  womrat7 float8,
  menrat7 float8,
  menrat3 float8,
  menrat1 float8,
  girlrat1 float8,
  girlrat3 float8,
  girlrat7 float8,
  boyrat7 float8,
  boyrat3 float8,
  boyrat1 float8,
  behmimp2 int4,
  behximp2 float8,
  poundprice float8,
  termmth int8,
  intrate int8,
  cashpric float8,
  jamcaspr float8,
  locurmnl float8,
  exchrate varchar(10),
  frencpri float8,
  frnprinl float8,
  nonpay float8,
  seneg float8,
  sl float8,
  wwc float8,
  slperton float8,
  tonpercr float8,
  portugus float8,
  manol int8,
  portug2 float8,
  deptamer float8,
  girl5 int4,
  boy5 int4,
  men5 int4,
  women5 int4,
  child5 int4,
  male5 int4,
  female5 int4,
  arrport2 int4,
  nsp int8,
  nna int8,
  infant3 int8,
  infants3 int4,
  infant1 int4,
  kleimerg float8,
  klemerg2 float8,
  adult5 int4,
  bahijelm float8,
  adult2 int4,
  adult4 int4,
  infant4 int4,
  voyagid2 float8,
  newmina int8,
  crew int2,
  guns2 int2,
  men7 int4,
  male7 int4,
  female7 int4,
  women7 int4,
  adult7 int4,
  boy7 int4,
  girl7 int4,
  child7 int4,
  voy3imp int8,
  slavcapt float8,
  majbycon float8,
  tabexpo int2,
  rice float8,
  slavemx7 float8,
  spanishamer float8,
  year25a float8,
  menvymrtrat float8,
  womenvymrtrat float8,
  boyvymrtrat float8,
  girlvymrtrat float8,
  cubaperiod float8,
  editor int8,
  mjbyptimp int8,
  newf int8,
  "filter_$" int8,
  natinim3 int8,
  mjselrg2 int8,
  newamreg int8,
  yearches int8,
  gc int8,
  angola int8,
  seafrica int8,
  regarrp2 int8,
  userid int8,
  xmimpflag int8,
  temp2 int8,
  flag2 int8,
  primarylast int8,
  primarylast1 int8,
  carib int8,
  natinim4 int8,
  us int8,
  mjselim2 int8,
  "temp" int8,
  CONSTRAINT pk_voyage PRIMARY KEY (iid),
  CONSTRAINT adpsale1 FOREIGN KEY (adpsale1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT adpsale2 FOREIGN KEY (adpsale2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT angola FOREIGN KEY (angola)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT arrport FOREIGN KEY (arrport)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT carib FOREIGN KEY (carib)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT constreg FOREIGN KEY (constreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT datepl FOREIGN KEY (datepl)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT deptreg FOREIGN KEY (deptreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT deptreg1 FOREIGN KEY (deptreg1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT destin FOREIGN KEY (destin)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT editor FOREIGN KEY (editor)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT embport2 FOREIGN KEY (embport2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT embpot FOREIGN KEY (embport)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT embreg FOREIGN KEY (embreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT embreg2 FOREIGN KEY (embreg2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fate2 FOREIGN KEY (fate2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fate3 FOREIGN KEY (fate3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fate4 FOREIGN KEY (fate4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "filter_$" FOREIGN KEY ("filter_$")
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_port_dep FOREIGN KEY (portdep)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT flag2 FOREIGN KEY (flag2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT gc FOREIGN KEY (gc)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majbuyimp FOREIGN KEY (majbyimp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majbuypt FOREIGN KEY (majbuypt)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majbuyrg FOREIGN KEY (majbuyrg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majbyim3 FOREIGN KEY (majbyim3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majselpt FOREIGN KEY (majselpt)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT majselrg FOREIGN KEY (majselrg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mjbyptimp FOREIGN KEY (mjbyptimp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mjselim2 FOREIGN KEY (mjselim2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mjselimp FOREIGN KEY (mjselimp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mjselreg1 FOREIGN KEY (mjselrg1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mjselrg2 FOREIGN KEY (mjselrg2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT natinim3 FOREIGN KEY (natinim3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT natinim4 FOREIGN KEY (natinim4)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT natinimp FOREIGN KEY (natinimp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "national" FOREIGN KEY ("national")
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT newamreg FOREIGN KEY (newamreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT newf FOREIGN KEY (newf)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT npafttra FOREIGN KEY (npafttra)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT plac1tra FOREIGN KEY (plac1tra)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT plac2tra FOREIGN KEY (plac2tra)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT plac3tra FOREIGN KEY (plac3tra)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT placcons FOREIGN KEY (placcons)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT portret FOREIGN KEY (portret)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT primarylast FOREIGN KEY (primarylast)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT primarylast1 FOREIGN KEY (primarylast1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regarrp FOREIGN KEY (regarrp)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regarrp2 FOREIGN KEY (regarrp2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regdis1 FOREIGN KEY (regdis1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regdis2 FOREIGN KEY (regdis2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regdis3 FOREIGN KEY (regdis3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regem1 FOREIGN KEY (regem1)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regem2 FOREIGN KEY (regem2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regem3 FOREIGN KEY (regem3)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regisreg FOREIGN KEY (regisreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT retrnreg FOREIGN KEY (retrnreg)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT rig FOREIGN KEY (rig)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT seafrica FOREIGN KEY (seafrica)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sla1port FOREIGN KEY (sla1port)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "temp" FOREIGN KEY ("temp")
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT temp2 FOREIGN KEY (temp2)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tontype FOREIGN KEY (tontype)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT us FOREIGN KEY (us)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT userid FOREIGN KEY (userid)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT xmimpflag FOREIGN KEY (xmimpflag)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT year100 FOREIGN KEY (year100)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT year25 FOREIGN KEY (year25)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT year5 FOREIGN KEY (year5)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT yearches FOREIGN KEY (yearches)
      REFERENCES dictionary (oid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;


-- Index: vid_index

-- DROP INDEX vid_index;

CREATE INDEX vid_index
  ON voyages
  USING btree
  (vid);


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


-- Index: index_main

-- DROP INDEX index_main;

CREATE INDEX index_main
  ON voyages_index
  USING btree
  (vid, voyage_index_id);


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


-- Index: index_voyage_slaves

-- DROP INDEX index_voyage_slaves;

CREATE INDEX index_voyage_slaves
  ON voyage_slaves
  USING btree
  (r_slave_id, r_voyage_index_id);

ALTER TABLE hibernate_sequence OWNER TO tasuser;
ALTER TABLE dictionary OWNER TO tasuser;
ALTER TABLE slaves OWNER TO tasuser;
ALTER TABLE voyage_slaves OWNER TO tasuser;
ALTER TABLE voyages OWNER TO tasuser;
ALTER TABLE voyages_index OWNER TO tasuser;





