SELECT 
	"s"."contact" 
FROM "student" AS "s" 
JOIN "group" "g" ON "g"."id" = ?
WHERE "s"."id" = ?