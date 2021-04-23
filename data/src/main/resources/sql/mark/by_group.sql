SELECT 
	"m"."id" AS "mark_id",
	"m"."mark" AS "mark_mark",
	"d"."id" AS "discipline_id",
	"d"."name" AS "discipline_name",
	"t"."id" AS "teacher_id",
	"p"."id" AS "data_id",
	"p"."first_name" AS "data_first_name",
	"p"."last_name" AS "data_last_name",
	"p"."patronymic" AS "data_patronymic",
	"s"."id" AS "student_id" 
FROM "mark" AS "m" 
JOIN "discipline" "d" ON "d"."id" = "m"."discipline" 
JOIN "teacher" "t" ON "t"."id" = "d"."teacher" 
JOIN "personal_data" "p" ON "p"."id" = "t"."data" 
JOIN "student" "s" ON "s"."id" = "m"."student" 
JOIN "group_students" "gs" ON "gs"."student" = "s"."id" 
JOIN "group" "g" ON "g"."id" = "gs"."group" 
WHERE "g"."id" = ?