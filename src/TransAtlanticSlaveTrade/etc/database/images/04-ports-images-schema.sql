CREATE TABLE ports_images
(
  image_id integer NOT NULL,
  port_id integer NOT NULL,
  CONSTRAINT ports_images_primary_key PRIMARY KEY (image_id, port_id),
  CONSTRAINT ports_images_image_id_fk FOREIGN KEY (image_id)
      REFERENCES images (image_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ports_images_port_id_fk FOREIGN KEY (port_id)
      REFERENCES ports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE ports_images OWNER TO tast;
