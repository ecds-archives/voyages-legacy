-- Function: publish(character varying)

-- DROP FUNCTION publish(character varying);

CREATE OR REPLACE FUNCTION publish(revisionname character varying)
  RETURNS boolean AS
$BODY$
DECLARE
        voyID integer; --VoyageId that is currently being processed  
        voyRec voyages%rowtype;  -- Record for FOR LOOP 1
BEGIN
        RAISE NOTICE '----------START----------';
	
        FOR voyRec IN SELECT * FROM voyages v WHERE  revision=-1 AND suggestion='f' 
        LOOP
             voyID:=voyRec.voyageid;
             RAISE NOTICE 'Processing voyageId: %', voyID;
             
             --Delete voyage record an FKs
	     DELETE FROM voyages WHERE voyageid=voyID AND ( (revision=0) OR (revision=1 AND suggestion='f'));

             --Update to active revision  
	     UPDATE voyages SET revision=1 WHERE voyageid=voyID AND revision=-1 and suggestion='f';
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
ALTER FUNCTION publish(character varying) OWNER TO tast;
