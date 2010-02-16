-- Function: publish()

-- DROP FUNCTION publish();

CREATE OR REPLACE FUNCTION publish()
  RETURNS boolean AS
$BODY$
DECLARE
        voyID integer; --VoyageId that is currently being processed 
        voyIID integer; --VoyageIid related to the cureently processed
        voyRec voyages%rowtype;  -- Record for FOR LOOP 1
        subMergeRec submissions_merge%rowtype;  --
        subEditRec submissions_edit%rowtype;
        subNewRec submissions_new%rowtype;
        sevID integer;
        subMergeVoyRec submissions_merge_voyages%rowtype;
        compareID integer; --used for comparasion
        backIID integer;--used for search back by iid
BEGIN
        RAISE NOTICE '----------START----------';
	
        FOR voyRec IN SELECT * FROM voyages v WHERE  revision=-1 AND suggestion='f' 
        LOOP
		voyID:=voyRec.voyageid;
		RAISE NOTICE 'Processing voyageId: %', voyID;

		SELECT iid into voyIID from voyages where revision=0 AND suggestion='t' AND voyageid=voyID;

		--Get the id column in summissions_edited_voyages table
		SELECT id into sevID from submissions_edited_voyages where voyage_iid = voyIID;

		--Get the records in merge table based on the sevID
		SELECT * from submissions_merge into subMergeRec WHERE editor_edited_voyage_id = sevID;
	     
		IF subMergeRec.submission_id IS NOT NULL     THEN
			FOR subMergeVoyRec IN SELECT * FROM submissions_merge_voyages WHERE submission_id=subMergeRec.submission_id
			LOOP
				SELECT voyage_iid into backIID FROM submissions_edited_voyages WHERE id= subMergeVoyRec.edited_voyage_id;
				SELECT voyageid into compareID FROM voyages where iid=backIID;
				IF compareID !=voyID	THEN
					DELETE FROM voyages WHERE iid=backIID;
				END IF;
			END LOOP;
			END IF;
	     
		--Delete voyage record an FKs
		DELETE FROM voyages WHERE voyageid=voyID AND ( (revision=0) OR (revision=1 AND suggestion='f'));
		
		--Update to active revision  
		UPDATE voyages SET revision=1 WHERE voyageid=voyID AND revision=-1 and suggestion='f';
		RAISE NOTICE 'After Update';
	END LOOP;

	FOR voyRec IN SELECT * FROM voyages v WHERE  revision!=1 
	LOOP
		voyIID:=voyRec.iid;

		--Get the id column in summissions_edited_voyages table
		SELECT id into sevID from submissions_edited_voyages where voyage_iid = voyIID;

		--Get the records in merge table based on the sevID
		SELECT * from submissions_merge into subMergeRec WHERE proposed_edited_voyage_id = sevID;
		
		SELECT * from submissions_edit into subEditRec WHERE new_edited_voyage_id = sevID;
		
		SELECT * from submissions_new into subNewRec WHERE new_edited_voyage_id = sevID;
	     
		IF subMergeRec.submission_id IS NOT NULL     THEN
			RAISE NOTICE 'Do not need to delete-left for further requirement';
			
		ELSIF subNewRec.submission_id IS NOT NULL	 THEN
			RAISE NOTICE 'Do not need to delete-left for further requirement';

		ELSIF subEditRec.submission_id IS NOT NULL	THEN
			RAISE NOTICE 'Do not need to delete-left for further requirement';

		ELSE
			DELETE FROM voyages where iid = voyIID;

		END IF;
		
	     
		RAISE NOTICE 'After Update';
	END LOOP;

	 
        RAISE NOTICE '---------END----------';
        
	RETURN true;
	
	--EXCEPTION
	--WHEN OTHERS THEN
	    --ROLLBACK;
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publish() OWNER TO tast;
