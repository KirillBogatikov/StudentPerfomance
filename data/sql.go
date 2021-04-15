package data

import (
    "StudentsPerfomance/security"
    "bytes"
    _ "embed"
    "github.com/google/uuid"
    pgx "github.com/jackc/pgx/v4"
    "github.com/jackc/pgx/v4/pgxpool"
    "github.com/jackc/pgx/v4/stdlib"
    "github.com/jmoiron/sqlx"
    "strconv"
    "strings"
    "time"
)

var (
    //go:embed sql/init.sql
    initScript string
)

var (
    schemaLoaded = false
)

func newUUID() *uuid.UUID {
    id := uuid.New()
    return &id
}

type SqlRepository struct {
    pool *pgxpool.Pool
    db  *sqlx.DB
    Repository
}

func parseURL(url string) (user, password, host, database string, port int) {
    //Delete first 11 symbols: "postgres://" protocol name
    url = url[11:]
    
    //first part is username. It ends with ":"
    passwordIndex := strings.Index(url, ":")
    user = url[:passwordIndex]
    
    //second part is password. It starts after ":" and ends with "@"
    hostIndex := strings.Index(url, "@")
    password = url[passwordIndex+1:hostIndex]
    
    //third part is host. It ends with ":"
    portIndex := strings.LastIndex(url, ":")
    host = url[hostIndex+1:portIndex]
    
    //fourth part is port
    databaseIndex := strings.Index(url, "/")
    port, _ = strconv.Atoi(url[portIndex+1:databaseIndex])
    
    //last part is database name
    database = url[databaseIndex+1:]
    
    return
}

func (s *SqlRepository) Connect(url string) {
    config, err := pgx.ParseConnectionString(url)
    if err != nil {
        panic(err)
    }
    
    //config.PreferSimpleProtocol = true
    pool, err := pgx.NewConnPool(pgx.ConnPoolConfig{
        ConnConfig: config,
        AfterConnect: nil,
        MaxConnections: 20,
        AcquireTimeout: 30 * time.Second,
    })
    if err != nil {
        panic(err)
    }
    
    nativeDB := stdlib.OpenDBFromPool(pool)
    
    s.pool = pool
    s.db = sqlx.NewDb(nativeDB, "pgx")
    
    if !schemaLoaded {
        _, err := s.db.Exec(initScript)
        if err != nil {
            panic(err)
        }
        schemaLoaded = true
    }
}

type SqlAuthRepository struct {
    SqlRepository
}

//go:embed sql/auth/check.sql
var checkCredentialsScript string
func (s SqlAuthRepository) CheckCredentials(credentials Credentials) (bool, error) {
    rows, err := s.db.Query(checkCredentialsScript)
    if err != nil {
        return false, err
    }
    
    if !rows.Next() {
        return false, nil
    }
    
    auth := Auth{}
    err = rows.Scan(&auth)
    if err != nil {
        return false, err
    }
    
    actualHash := security.GetPasswordHash(credentials.Password)
    if bytes.Equal(auth.PasswordHash, actualHash) {
        return auth.Login == credentials.Login, nil
    }
    
    return false, nil
}

type SqlTeacherRepository struct {
    SqlRepository
}

func (s SqlRepository) list(script string, args... interface{}) ([]Teacher, error) {
    rows, err := s.db.Query(script, args...)
    if err != nil {
        return nil, err
    }
    
    list := make([]Teacher, 0)
    for rows.Next() {
        t := Teacher{}
        err := rows.Scan(&t)
        if err != nil {
            return list, err
        }
        
        list = append(list, t)
    }
    
    return list, nil
}

//go:embed sql/teacher/list.sql
var teacherListScript string
func (s SqlTeacherRepository) List(offset, limit int) ([]Teacher, error) {
    return s.list(teacherListScript, offset, limit)
}

//go:embed sql/teacher/search.sql
var teacherSearchScript string
func (s SqlTeacherRepository) Search(query string) ([]Teacher, error) {
    return s.list(teacherSearchScript, query)
}

//go:embed sql/teacher/create.sql
var teacherSaveScript string
func (s SqlTeacherRepository) Save(teacher Teacher) (*uuid.UUID, error) {
    var id *uuid.UUID
    if teacher.Id == nil {
        id = newUUID()
    } else {
        id = teacher.Id
    }
    
    _, err := s.db.NamedExec(teacherSearchScript, teacher)
    if err != nil {
        return nil, err
    }
    
    return id, nil
}

func (s SqlTeacherRepository) Delete(uuid uuid.UUID) error {
    panic("implement me")
}

