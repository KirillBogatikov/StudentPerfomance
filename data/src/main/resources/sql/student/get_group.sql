SELECT 
	"g"."id" AS "group_id", 
	"g"."code" AS "group_code",
	"g"."duration" AS "group_duration"
FROM "group" AS "g" 
JOIN "group_students" AS "gs" ON "gs"."group" = "g"."id" 
WHERE "gs"."student" = ?