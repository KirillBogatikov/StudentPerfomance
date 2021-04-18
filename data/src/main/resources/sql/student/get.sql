SELECT 
	"s"."id" AS "student_id",
	"d"."id" AS "data_id",
	"d"."first_name" AS "data_first_name",
	"d"."last_name" AS "data_last_name",
	"d"."patronymic" AS "data_patronymic",
	"c"."id" AS "contact_id",
	"c"."phone" AS "contact_phone",
	"c"."email" AS "contact_email" 
FROM "student" AS "s" 
JOIN "contact" AS "c" ON "s"."contact" = "c"."id" 
JOIN "personal_data" AS "d" ON "s"."data" = "d"."id"
WHERE "s"."id" = ?