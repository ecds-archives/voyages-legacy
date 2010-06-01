--FATES
UPDATE fates SET name = 'Other (specify in note)' WHERE id =999;

--RESISTANCE
INSERT INTO resistance VALUES(9, 'Other (specify in note)');

--NATIONS

ALTER TABLE voyages DROP CONSTRAINT fk_1_natinimp;

ALTER TABLE voyages
  ADD CONSTRAINT fk_1_natinimp FOREIGN KEY (natinimp)
      REFERENCES nations (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE NO ACTION;
UPDATE nations set name = 'Other (specify in note)' WHERE id ='30';
Insert into nations values(99, 'Other (specify in note)',99);
UPDATE nations set order_num = id;
UPDATE voyages set natinimp = '15' where natinimp ='13';



--VESSEL_RIGS
INSERT INTO vessel_rigs VALUES(99, 'Other (specify in note)');

--TON_TYPE
INSERT INTO ton_type VALUES(99, 'Other (specify in note)');