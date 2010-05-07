-- Function: delete_mergevoyage(integer)

-- DROP FUNCTION delete_mergevoyage(integer);

CREATE OR REPLACE FUNCTION delete_mergevoyage(integer)
  RETURNS boolean AS
$BODY$
DECLARE
	sid ALIAS FOR $1;
	subMergeRec submissions_merge%rowtype;  -- Record for submissions_merge
	subEditor submission_editors%rowtype; -- Record for submission_editors
	subMergeVoy submissions_merge_voyages%rowtype; --Record for submissions_merge_voyages%
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
		
		SELECT * FROM submissions_merge into subMergeRec WHERE submission_id = sid;
		SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subMergeRec.proposed_edited_voyage_id;

		IF voyIID IS NOT NULL	THEN 
			DELETE from voyages where iid= voyIID;
		ELSE
			DELETE FROM submissions_edited_voyages WHERE id = subMergeRec.proposed_edited_voyage_id;
		END IF;
		
		IF subMergeRec.editor_edited_voyage_id IS NOT NULL	THEN
			SELECT voyage_iid into voyIID FROM submissions_edited_voyages WHERE id = subMergeRec.editor_edited_voyage_id;

			IF voyIID IS NOT NULL	THEN 
				SELECT voyageid into voyID from voyages where iid = voyIID;
				DELETE from voyages where iid = voyIID;
			ELSE
				DELETE FROM submissions_edited_voyages WHERE id = subMergeRec.editor_edited_voyage_id;
			END IF;
			
			DELETE FROM voyages where voyageid = voyID AND revision!='1';
		END IF;

		FOR subMergeVoy IN SELECT * FROM submissions_merge_voyages WHERE submission_id = sid
		LOOP
			IF subMergeVoy.edited_voyage_id IS NOT NULL THEN
				SELECT voyage_iid into voyIID from submissions_edited_voyages where id = subMergeVoy.edited_voyage_id;
				DELETE FROM submissions_edited_voyages WHERE id = subMergeVoy.edited_voyage_id;
				DELETE FROM voyages WHERE iid = voyIID;
			ELSE
				DELETE FROM submissions_merge_voyages WHERE submission_id = sid;
			END IF;
		END LOOP;
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
ALTER FUNCTION delete_mergevoyage(integer) OWNER TO tast;
