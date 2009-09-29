CREATE TABLE glossary
(
  id bigint NOT NULL,
  term character varying(200) NOT NULL,
  description text NOT NULL,
  CONSTRAINT glossary_pkey PRIMARY KEY (id)
);

ALTER TABLE glossary OWNER TO tast;