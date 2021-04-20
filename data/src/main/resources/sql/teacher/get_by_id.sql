SELECT 
	"t"."id" AS "teacher_id",
	"a"."id" AS "auth_id",
	"a"."login" AS "auth_login",
	"a"."password" AS "auth_password",
	"d"."id" AS "data_id",
	"d"."first_name" AS "data_first_name",
	"d"."last_name" AS "data_last_name",
	"d"."patronymic" AS "data_patronymic" 
FROM "teacher" AS "t" 
JOIN "auth_data" AS "a" ON "t"."auth" = "a"."id" 
JOIN "personal_data" AS "d" ON "t"."data" = "d"."id" 
WHERE "t"."id" = ?