CREATE TABLE images
(
	image_id integer NOT NULL,
	file_name character varying(256),
	title character varying(200) NOT NULL,
	description text,
	width integer,
	height integer,
	mime_type character varying(100),
	creator character varying(200),
	language character(2),
	size integer,
	source character varying(500),
	comments text,
	other_references character varying(500),
	emory boolean NOT NULL,
	emory_location character varying(500),
	authorization_status integer DEFAULT 0 NOT NULL,
	image_status integer DEFAULT 0 NOT NULL,
	ready_to_go boolean DEFAULT false NOT NULL,
	order_num integer DEFAULT 0 NOT NULL,
	category integer NOT NULL,
	date integer NOT NULL,
	external_id character varying DEFAULT 20,
	CONSTRAINT images_primary_key PRIMARY KEY (image_id),
	CONSTRAINT images_categories FOREIGN KEY (category)
		REFERENCES image_categories (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION    
);

ALTER TABLE images OWNER TO tast;