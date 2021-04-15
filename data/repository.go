package data

import "github.com/google/uuid"

type Credentials struct {
    Login string
    Password string
}

type Repository interface {
    Connect(url string)
}

type AuthRepository interface {
    CheckCredentials(Credentials) (bool, error)
    Repository
}

type TeacherRepository interface {
    List(offset, limit int) ([]Teacher, error)
    Search(query string) ([]Teacher, error)
    Save(Teacher) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}

type DisciplineRepository interface {
    List(offset, limit int) ([]Discipline, error)
    Search(query string) ([]Discipline, error)
    Save(Discipline) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}

type GroupRepository interface {
    List() ([]Group, error)
    Search(query string) ([]Group, error)
    Save(Group) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}

type StudentRepository interface {
    List() ([]Student, error)
    Search(query string) ([]Student, error)
    Save(student Student) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}

type PlanRepository interface {
    List() ([]Plan, error)
    Search(query string) ([]Plan, error)
    Save(Plan) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}

type MarkRepository interface {
    ListByGroup(Group) ([]Mark, error)
    ListByStudent(Student) ([]Mark, error)
    Save(Plan) (*uuid.UUID, error)
    Delete(uuid.UUID) error
    Repository
}