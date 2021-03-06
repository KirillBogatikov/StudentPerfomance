CREATE TABLE IF NOT EXISTS "auth_data" (
    "id" uuid PRIMARY KEY,
    "login" text NOT NULL,
    "password" bytea NOT NULL
);

CREATE TABLE IF NOT EXISTS "personal_data" (
    "id" uuid PRIMARY KEY,
    "first_name" text NOT NULL,
    "last_name" text NOT NULL,
    "patronymic" text
);

CREATE TABLE IF NOT EXISTS "contact" (
    "id" uuid PRIMARY KEY,
    "phone" text,
    "email" text
);

CREATE TABLE IF NOT EXISTS "teacher" (
    "id" uuid PRIMARY KEY,
    "auth" uuid REFERENCES "auth_data" ("id") ON DELETE CASCADE,
    "data" uuid REFERENCES "personal_data"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "student" (
    "id" uuid PRIMARY KEY,
    "data" uuid REFERENCES "personal_data"("id") ON DELETE CASCADE,
    "contact" uuid REFERENCES "contact"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "group" (
    "id" uuid PRIMARY KEY,
    "code" text,
    "duration" smallint
);

CREATE TABLE IF NOT EXISTS "group_students" (
    "id" uuid PRIMARY KEY,
    "group" uuid REFERENCES "group"("id") ON DELETE CASCADE,
    "student" uuid REFERENCES "student"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "discipline" (
    "id" uuid PRIMARY KEY,
    "teacher" uuid REFERENCES "teacher"("id") ON DELETE CASCADE,
    "name" text
);

CREATE TABLE IF NOT EXISTS "mark" (
    "id" uuid PRIMARY KEY,
    "student" uuid REFERENCES "student"("id") ON DELETE CASCADE,
    "discipline" uuid REFERENCES "discipline"("id") ON DELETE CASCADE,
    "mark" smallint
);