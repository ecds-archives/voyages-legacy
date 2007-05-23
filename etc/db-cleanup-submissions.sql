ALTER TABLE submissions_attribute_notes DROP CONSTRAINT fk_submissions_attribute_notes_to_submissions;
ALTER TABLE submissions_attribute_notes RENAME submission_id  TO edited_voyage_id;
ALTER TABLE submissions_attribute_notes ADD CONSTRAINT fk_submissions_attribute_notes_to_submissions_edited_voyage FOREIGN KEY (edited_voyage_id) REFERENCES submissions_edited_voyages (id)    ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE submissions_attribute_notes ALTER COLUMN edited_voyage_id SET STATISTICS -1;


truncate submissions, submissions_new, submissions_edit, submissions_merge, submissions_attribute_notes,
submissions_merge_voyages, submissions_sources, submissions_sources_books, submissions_sources_papers,
submissions_sources_others, submissions_edited_voyages