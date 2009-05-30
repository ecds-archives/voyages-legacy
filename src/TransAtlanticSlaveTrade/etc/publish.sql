-- Function: publish(character varying)

-- DROP FUNCTION publish(character varying);

CREATE OR REPLACE FUNCTION publish(revisionname character varying)
  RETURNS boolean AS
$BODY$
DECLARE
	revID integer; --Next Revision
	curRevID integer; --Current Revision
        voyID integer; --VoyageId that is currently being processed  
        
        voyRec voyages%rowtype;  -- Record for FOR LOOP 1
        voyRec2 voyages%rowtype;  -- Record for FOR LOOP 2
        voyIID integer; --VoyageIId for LOOP 1
        eID integer; --Edit ID for LOOP 1
        
BEGIN
        RAISE NOTICE '----------START----------';

        --Get Next Revision
	SELECT nextval('revisions_sequence') INTO revID;
	RAISE NOTICE 'Next Revision: %', revID;

	--Get Current Revision
	SELECT max(revision) INTO curRevID FROM voyages;
	RAISE NOTICE 'Current Revision: %', curRevID;

        --Create new revision record in revisions table
	INSERT INTO revisions (id, name) values (revID, revisionName);
	RAISE NOTICE 'Created New Revision Record: % %', revID, revisionName;

      --Remove constraints and indexes 
      select publishpreupdate();

	--Update revision to next number
        RAISE NOTICE 'Start Update';
        UPDATE voyages SET revision=revID WHERE revision > 0 AND suggestion='f'; 
        RAISE NOTICE 'End Update';

        --Restore constraints and indexes
        select publishpostupdate();

	
        --Update and delete records for new revision
        FOR voyRec IN SELECT voyageid FROM voyages v WHERE  revision=-1 AND suggestion='f' 
        LOOP
             voyID:=voyRec.voyageid;
             RAISE NOTICE 'Processing voyageId: %', voyID;
             
             UPDATE voyages SET revision=revID WHERE revision=-1 and suggestion='f' AND voyageid=voyID;
             RAISE NOTICE 'After Update';
             
             
             --Delete voyage record an FKs
	     FOR voyRec2 IN SELECT iid FROM voyages  WHERE voyageid=voyID AND ( (revision=-1 AND suggestion='f') OR (revision=0) ) 	     LOOP
	     voyIID:=voyRec2.iid;
	     SELECT id INTO eID FROM submissions_edited_voyages where voyage_iid= voyIID;

             DELETE FROM submissions_edit WHERE old_edited_voyage_id=eID;
	     DELETE FROM submissions_edited_voyages where voyage_iid= voyIID;
	     DELETE FROM voyages  WHERE voyageid=voyID AND ( (revision=curRevID  AND  suggestion='f') OR (revision=0) );
	     END LOOP;

	     RAISE NOTICE 'After Delete';
        END LOOP;

	 
        RAISE NOTICE '---------END----------';
        
	RETURN true;
	
	EXCEPTION
	WHEN OTHERS THEN
	    ROLLBACK;
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publish(character varying) OWNER TO tast;
