CREATE TABLE faqs
(
  id bigint NOT NULL,
  cat_id bigint NOT NULL,
  question character varying(500) NOT NULL,
  answer character varying(5000) NOT NULL,
  CONSTRAINT pk_faqs PRIMARY KEY (id),
  CONSTRAINT fk_faqs_categories FOREIGN KEY (cat_id)
      REFERENCES faqs_categories (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE faqs OWNER TO tast;