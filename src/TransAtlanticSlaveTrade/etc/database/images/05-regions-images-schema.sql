CREATE TABLE regions_images
(
  region_id integer NOT NULL,
  image_id integer NOT NULL,
  CONSTRAINT regions_images_primary_key PRIMARY KEY (region_id, image_id),
  CONSTRAINT regions_images_image_id_fk FOREIGN KEY (image_id)
      REFERENCES images (image_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT regions_images_region_id_fk FOREIGN KEY (region_id)
      REFERENCES regions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE regions_images OWNER TO tast;
