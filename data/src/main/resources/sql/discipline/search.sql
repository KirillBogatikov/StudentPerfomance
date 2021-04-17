SELECT 
	"d"."id" AS "discipline_id", 
	"d"."name" AS "discipline_name"   
FROM "discipline" AS "d"  
WHERE "d"."name" ~ ? 
OFFSET ? LIMIT ?