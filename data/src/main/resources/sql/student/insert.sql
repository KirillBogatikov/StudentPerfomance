INSERT INTO "personal_data" ("id", "first_name", "last_name", "patronymic") 
	VALUES (?, ?, ?, ?);

INSERT INTO "contact" ("id", "phone", "email")  
	VALUES (?, ?, ?);

INSERT INTO "student" ("id", "data", "contact") 
    VALUES (?, ?, ?);