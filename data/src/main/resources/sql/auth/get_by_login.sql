SELECT 
	"a"."id" AS "auth_id", 
    "a"."login" as "auth_login", 
    "a"."password" as "auth_password" 
FROM "auth_data" AS "a"
JOIN "teacher" AS "t" ON "t"."auth" = "a"."id" 
WHERE "a"."login" = ?