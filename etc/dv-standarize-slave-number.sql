CREATE or replace FUNCTION estimate(vid int8, formula "varchar") RETURNS float8 AS
'
declare 
	cur refcursor;
	value float8;
begin
	open cur for execute ''select '' || formula || '' from voyages where iid='' || vid;
	fetch cur into value;
	return value;
end
'
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION estimate(int8, "varchar") OWNER TO tast;
