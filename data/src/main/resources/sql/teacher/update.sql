UPDATE "auth_data" SET  
	"login" = ?, 
	"password" = ?  
WHERE "id" = ?;
    
UPDATE "personal_data" SET 
	"first_name" = ?,
	"last_name" = ?,
	"patronymic" = ? 
WHERE "id" = ?;