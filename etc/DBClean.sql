-- super fast
truncate voyage_slaves, voyages_index, voyages, slaves, dictionary, estimates;

-- super slow
delete from voyage_slaves;
delete from voyages_index;
delete from voyages;
delete from slaves;
delete from dictionary;
