SELECT 
	"d"."id" AS "discipline_id",
	"d"."name" AS "discipline_name",
	"t"."id" AS "teacher_id",
	"p"."id" AS "data_id",
	"p"."first_name" AS "data_first_name",
	"p"."last_name" AS "data_last_name",
	"p"."patronymic" AS "data_patronymic" 
FROM "discipline" AS "d" 
JOIN "teacher" "t" ON "t"."id" = "d"."teacher" 
JOIN "personal_data" AS "p" ON "t"."data" = "p"."id"