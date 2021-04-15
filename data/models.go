package data

import (
    "github.com/google/uuid"
    "time"
)

type Auth struct {
    Id           *uuid.UUID `db:"auth_id"`
    Login        string    `db:"auth_login"`
    PasswordHash []byte    `db:"auth_password"`
}

type PersonalData struct {
    Id         *uuid.UUID `db:"personal_id"`
    FirstName  string    `db:"personal_first_name"`
    LastName   string    `db:"personal_last_name"`
    Patronymic string    `db:"personal_patronymic"`
}

type Contact struct {
    Id    *uuid.UUID `db:"contact_id"`
    Phone string    `db:"contact_phone"`
    Email string    `db:"contact_email"`
}

type Discipline struct {
    Id      *uuid.UUID `db:"discipline_id"`
    Name    string    `db:"discipline_name"`
    Teacher *Teacher
    Hours   int `db:"discipline_hours"`
}

func (d *Discipline) IsExtended() bool {
    return d.Teacher != nil && d.Hours > 0
}

type Teacher struct {
    Id          *uuid.UUID `db:"teacher_id"`
    Disciplines []Discipline
    *Auth
    *PersonalData
}

type Student struct {
    Id *uuid.UUID `db:"student_id"`
    *PersonalData
    *Contact
}

type Group struct {
    Id       *uuid.UUID `db:"group_id"`
    Code     string    `db:"group_code"`
    Duration int       `db:"group_duration"`
    Students []Student
}

type Plan struct {
    Id         *uuid.UUID `db:"plan_id"`
    Group      Group
    Semester   int `db:"semester"`
    Discipline []Discipline
}

type Mark struct {
    Id         *uuid.UUID `db:"mark_id"`
    Student    Student
    Discipline Discipline
    Mark       int       `db:"mark"`
    Date       time.Time `db:"date"`
}
