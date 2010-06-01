-- Function: getsubmissionid(integer)

-- DROP FUNCTION getsubmissionid(integer);

CREATE OR REPLACE FUNCTION getsubmissionid(IN integer, OUT integer)
  RETURNS integer AS
$BODY$
DECLARE
	voyID ALIAS FOR $1;
	subid ALIAS FOR $2;
	voyIId integer;	--voyage iid column
	sevid integer;	--submissions_edited_voyages id column
	result integer;
BEGIN
        RAISE NOTICE '----------START----------';
        
        SELECT iid into voyIId FROM voyages WHERE voyageid = voyID AND revision = '-1' AND suggestion = 't';
        
	SELECT id into sevid FROM submissions_edited_voyages WHERE voyage_iid = voyIId;
	
	SELECT submission_id into subid FROM submissions_new WHERE new_edited_voyage_id = sevid;

	IF subid IS NULL	THEN
		SELECT submission_id into subid FROM submissions_edit WHERE new_edited_voyage_id = sevid;
	END IF;

	IF subid IS NULL	THEN
		SELECT submission_id into subid FROM submissions_merge WHERE proposed_edited_voyage_id = sevid;
	END IF;	

	RAISE NOTICE '---------END----------';
        
        
	
	--EXCEPTION
	--WHEN OTHERS THEN
	    --ROLLBACK;
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION getsubmissionid(integer) OWNER TO tast;
