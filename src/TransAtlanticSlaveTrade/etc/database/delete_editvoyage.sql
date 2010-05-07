-- Function: delete_editvoyage(integer)

-- DROP FUNCTION delete_editvoyage(integer);

CREATE OR REPLACE FUNCTION delete_editvoyage(integer)
  RETURNS boolean AS
$BODY$
DECLARE
	sid ALIAS FOR $1;
	subEditRec submissions_edit%rowtype;  -- Record for submissions_new
	subEditor submission_editors%rowtype; -- Record for submission_editors
	voyIID integer;	--used to retrievl voyage
	voyID integer;	--used to delete accepted voyage
	
BEGIN
        RAISE NOTICE '----------START----------';
        
        FOR subEditor IN SELECT * FROM submission_editors WHERE  submission_id = sid
        LOOP
		SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subEditor.edited_voyage_id;
		IF voyIID IS NOT NULL	THEN 
			DELETE from voyages where iid= voyIID;
		ELSE
			DELETE from submissions_edited_voyages where id = subEditor.edited_voyage_id;
		END IF;
        END LOOP;
        
	SELECT * FROM submissions_edit into subEditRec WHERE submission_id = sid;
	SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subEditRec.old_edited_voyage_id;
	SELECT voyageid into voyID from voyages where iid = voyIID;
	
	SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subEditRec.new_edited_voyage_id;

	IF voyIID IS NOT NULL	THEN 
		DELETE from voyages where iid= voyIID;
	ELSE
		DELETE FROM submissions_edited_voyages WHERE id = subEditRec.new_edited_voyage_id;
	END IF;
	
	IF subEditRec.editor_edited_voyage_id IS NOT NULL	THEN
		SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subEditRec.editor_edited_voyage_id;

		IF voyIID IS NOT NULL	THEN 
			DELETE from voyages where iid = voyIID;
		ELSE
			DELETE FROM submissions_edited_voyages WHERE id = subEditRec.editor_edited_voyage_id;
		END IF;
	
	END IF;
	DELETE FROM submissions_edited_voyages WHERE id = subEditRec.old_edited_voyage_id;

	
	DELETE from voyages where voyageid = voyID;
	
	DELETE FROM submissions WHERE id =sid;
        
	RAISE NOTICE '---------END----------';
        
        
        
	RETURN true;
	
	--EXCEPTION
	--WHEN OTHERS THEN
	    --ROLLBACK;
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION delete_editvoyage(integer) OWNER TO tast;
