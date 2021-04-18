@echo off

if [%1]==[compile] ( mvn clean package )
java --enable-preview -jar -server rest/target/rest-0.0.1.jar {\"secret\":\"oIdF8hWE/HxWlXNtzEOfn3oY8c6jfL8u5+Pvr/BQZ6c=\",\"salt\":\"pkipt-it-college\",\"salt_position\":3,\"jdbc\":\"postgresql://localhost:5432/student_perfomance?ssl=false&user=student_perfomance&password=student_perfomance\",\"host\":\"localhost\"}
