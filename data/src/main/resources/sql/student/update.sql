UPDATE "personal_data" SET  
	"first_name" = ?, 
	"last_name" = ?, 
	"patronymic" = ? 
WHERE "id" = ?;
    
UPDATE "contact" SET 
	"phone" = ?,
	"email" = ?  
WHERE "id" = ?;