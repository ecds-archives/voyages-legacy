TRUNCATE voyages_index, voyages;

TRUNCATE voyages_index, voyages, areas, regions, ports;

TRUNCATE voyage_slaves, voyages_index, voyages, slaves, estimates;

TRUNCATE voyages, slaves, voyage_slaves, voyages_index, areas, regions, ports, ports_images, regions_images;

TRUNCATE voyages, voyage_slaves, voyages_index, estimates, estimates_export_regions, estimates_import_areas, estimates_import_regions, estimates_nations;

TRUNCATE voyages_index, voyages, areas, regions, ports, ports_images, regions_images, submissions, submissions_new, submissions_edit, submissions_merge, submissions_attribute_notes,
submissions_merge_voyages, submissions_sources, submissions_sources_books, submissions_sources_papers,
submissions_sources_others, submissions_edited_voyages;