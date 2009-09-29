CREATE TABLE image_categories
(
	id integer NOT NULL,
	"name" character varying(255),
	CONSTRAINT pk_categories PRIMARY KEY (id)
);

ALTER TABLE image_categories OWNER TO tast;