INSERT INTO "auth_data" ("id", "login", "password") 
    VALUES (?, ?, ?);
    
INSERT INTO "personal_data" ("id", "first_name", "last_name", "patronymic")
	VALUES (?, ?, ?, ?);
	
INSERT INTO "teacher" ("id", "auth", "data")
	VALUES (?, ?, ?);