CREATE TABLE people_images
(
  person_id integer NOT NULL,
  image_id integer NOT NULL,
  CONSTRAINT people_images_primary_key PRIMARY KEY (person_id, image_id),
  CONSTRAINT people_images_image_id_fk FOREIGN KEY (image_id)
      REFERENCES images (image_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT people_images_person_id_fk FOREIGN KEY (person_id)
      REFERENCES people (person_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE people_images OWNER TO tast;
