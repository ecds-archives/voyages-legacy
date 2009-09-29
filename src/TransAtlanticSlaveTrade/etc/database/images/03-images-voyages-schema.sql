CREATE TABLE images_voyages
(
  image_id integer NOT NULL,
  voyageid integer NOT NULL,
  CONSTRAINT pk_images_voyageids PRIMARY KEY (image_id, voyageid),
  CONSTRAINT fk_images_voyageids_to_images FOREIGN KEY (image_id)
      REFERENCES images (image_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE images_voyages OWNER TO tast;