--This script alters the column lengths of some title fields. Read about it in the Release 2_4_2 notes and
--the change comes from Service Ticket INC01472260

ALTER table submissions_sources_others ALTER COLUMN title TYPE character varying(225); 

ALTER table submissions_sources_books ALTER COLUMN title TYPE character varying(225);