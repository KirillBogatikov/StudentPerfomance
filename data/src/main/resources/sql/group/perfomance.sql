SELECT
	"m"."id" AS "mark_id",
	"m"."mark" AS "mark_mark",
	"m"."date" AS "mark_date",
	"d"."id" AS "discipline_id",
	"d"."name" AS "discipline_name",
	"pl"."first_name" AS "personal_first_name",
	"pl"."last_name" AS "personal_last_name",
	"pl"."patronymic" AS "personal_patronymic",
	"c"."phone" AS "contact_phone",
	"c"."email" AS "contact_email" 
FROM "mark" AS "m"  
JOIN "plan_discipline" "pd" ON "pd"."id" = "m"."plan_discipline" 
JOIN "discipline" "d" ON "d"."id" = "pd"."discipline" 
JOIN "plan" "p" ON "p"."id" = "pd"."plan" 
JOIN "student" "s" ON "s"."id" = "m"."student" 
JOIN "personal" "pl" ON "pl"."id" = "s"."personal"
JOIN "contact" "c" ON "c"."id" = "s"."contact" 
WHERE "p"."group" = ? AND "p"."semester" = ? AND "d"."id" IN ?