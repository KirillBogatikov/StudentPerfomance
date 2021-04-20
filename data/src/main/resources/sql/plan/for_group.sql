SELECT 
	"p"."id" AS "plan_id",
	"p"."semester" AS "plan_semester",
	"g"."id" AS "group_id",
	"g"."code" AS "group_code",
	"g"."duration" AS "group_duration"
FROM "plan" AS "p" 
JOIN "group" "g" ON "g"."id" = "p"."group" 
WHERE "g"."id" = ?