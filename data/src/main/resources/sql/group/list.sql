SELECT 
	"g"."id" AS "group_id",
	"g"."code" AS "group_code",
	"g"."duration" AS "group_duration"
FROM "group" AS "g" 
OFFSET ? LIMIT ?