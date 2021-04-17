SELECT 
	"s"."id" AS "student_id",
	"d"."first_name" AS "personal_first_name",
	"d"."last_name" AS "personal_last_name",
	"d"."patronymic" AS "personal_patronymic",
	"c"."phone" AS "contact_phone", 
	"c"."email" AS "contact_email" 
FROM "student" AS "s" 
JOIN "personal_data" "d" ON "d"."id" = "s"."data" 
JOIN "contact" "c" ON "c"."id" = "s"."contact" 
JOIN "group_students" "gs" ON "gs"."student" = "s"."id" 
WHERE "gs"."group" = ? OFFSET ? LIMIT ?