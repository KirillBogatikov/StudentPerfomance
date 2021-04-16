SELECT 
	"i"."id" AS "import_id",
	"i"."table" AS "import_table",
	"i"."version" AS "import_version",
	"i"."date" AS "import_date" 
FROM "imports" AS "i" 
WHERE "table" = ? 
ORDER BY "date" DESC 
LIMIT 1