CREATE TABLE faqs_categories
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  order_num integer NOT NULL,
  CONSTRAINT pk_faqs_categories PRIMARY KEY (id)
);

ALTER TABLE faqs_categories OWNER TO tast;