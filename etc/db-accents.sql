SELECT 
	REPLACE(TRANSLATE(shipname,
	'abcdefghijklmnopqrtsuvwxyzABCDEFGHIJKLMNOPQRTSUVWXYZ0123456789()_''-,?.&/[]',
	'                                                                           '), ' ', ''),
FROM voyages
WHERE '' <>
	REPLACE(TRANSLATE(shipname,
	'abcdefghijklmnopqrtsuvwxyzABCDEFGHIJKLMNOPQRTSUVWXYZ0123456789()_''-,?.&/[]',
	'                                                                           '), ' ', '')