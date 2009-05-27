-- Function: publish(character varying)

-- DROP FUNCTION publish(character varying);

CREATE OR REPLACE FUNCTION publish(revisionname character varying)
  RETURNS boolean AS
$BODY$
DECLARE
	revID integer; --Next Revision
	curRevID integer; --Current Revision
        voyRec voyages%rowtype;  -- Record for FOR LOOP
        voyID integer; --VoyageId that is currently being processed


BEGIN
        START TRANSACTION;
        RAISE NOTICE '----------Transaction Start----------';

        --Create a savepoint incase of eror
        SAVEPOINT publish_start;


        --Get Next Revision
	revID = nextval('revisions_sequence');
	RAISE NOTICE 'Next Revision: %', revID;

	--Get Current Revision
	SELECT max(revision) INTO curRevID FROM voyages;
	RAISE NOTICE 'Current Revision: %', curRevID;

        --Create new revision record in revisions table
	INSERT INTO revisions (id, name) values (revID, revisionName);
	RAISE NOTICE 'Created New Revision Record: %', revID;

	--Copy all records except for revised voyages to next version
        FOR voyRec IN SELECT * FROM voyages WHERE revison > 0 AND suggestion='f' 
        LOOP
             voyRec.revision:=revID;
             voyRec.iid:=nextval('voyages_iid_seq');
             
             insert into voyages values(voyRec.*);
        END LOOP;
	
        --Update and delete records for new revision
        FOR voyRec IN SELECT voyageid FROM voyages v WHERE  revision=-1 AND suggestion='f' 
        LOOP
             voyID:=voyRec.voyageid;
             RAISE NOTICE 'Processing voyageId: %', voyID;
             
             UPDATE voyages SET revision=revID WHERE revision=-1 and suggestion='f' AND voyageid=voyID;
             
	     DELETE FROM voyages  WHERE revision=curRevID  AND voyageid=voyID;
        END LOOP;

	 
        --Release savepoint and end transaction
        RELEASE SAVEPOINT publish_start;  
        COMMIT; 
        RAISE NOTICE '---------Transaction End----------';
        
	RETURN true;
	
	EXCEPTION 
	WHEN OTHERS THEN
	    ROLLBACK TO publish_start;
	    RAISE NOTICE 'Publish Failed';
	    RETURN false;
	    
end$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION publish(character varying) OWNER TO tast;
