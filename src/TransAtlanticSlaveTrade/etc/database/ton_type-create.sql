CREATE TABLE ton_type
(
  id bigint NOT NULL,
  "name" character varying(200),
  CONSTRAINT primary_key_ton_type PRIMARY KEY (id)
);
ALTER TABLE ton_type OWNER TO tast;
GRANT ALL ON TABLE ton_type TO tast;

insert into ton_type(id, name) values(1,    	'Portuguese');
insert into ton_type(id, name) values(2,    	'Brazilian');
insert into ton_type(id, name) values(3,    	'Spanish');
insert into ton_type(id, name) values(4,    	'French');
insert into ton_type(id, name) values(5,    	'U.S.A.');
insert into ton_type(id, name) values(6,    	'Uruguayan');
insert into ton_type(id, name) values(7,    	'Dutch');
insert into ton_type(id, name) values(8,    	'Hanse Towns, Brandenburg');
insert into ton_type(id, name) values(9,    	'Argentinian');
insert into ton_type(id, name) values(10,  	'Swedish');
insert into ton_type(id, name) values(11,  	'Russian');
insert into ton_type(id, name) values(12,  	'Danish');
insert into ton_type(id, name) values(13,  	'English 1');
insert into ton_type(id, name) values(14,  	'English 2');
insert into ton_type(id, name) values(15,  	'English 3');
insert into ton_type(id, name) values(17,  	'English 4');
insert into ton_type(id, name) values(18,  	'Sardinian');
insert into ton_type(id, name) values(19,  	'Norwegian');
insert into ton_type(id, name) values(20,  	'Mexican');
insert into ton_type(id, name) values(21,  	'English RAC');
insert into ton_type(id, name) values(22,  	'English pre-1775');