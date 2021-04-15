package main

import (
    "StudentsPerfomance/data"
    "StudentsPerfomance/security"
    _ "embed"
    "fmt"
)

//go:embed data/sql/init.sql
var text string

func main() {
    t := data.SqlTeacherRepository{}
    t.Connect("postgres://student_perfomance:student_perfomance@localhost:5432/student_perfomance")
    
    id, err := t.Save(data.Teacher{
        Auth: &data.Auth{
            Login:    "1234",
            PasswordHash: security.GetPasswordHash("12345678"),
        },
    })
    fmt.Printf("%v, %v", id, err)
}