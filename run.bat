@echo off

if [%1]==[compile] ( D:\programs\maven\bin\mvn clean package )
java --enable-preview -jar -server rest/target/rest-0.0.1.jar {\"secret\":\"oIdF8hWE/HxWlXNtzEOfn3oY8c6jfL8u5+Pvr/BQZ6c=\",\"salt\":\"pkipt-it-college\",\"salt_position\":3,\"jdbc\":\"postgresql://ec2-54-155-208-5.eu-west-1.compute.amazonaws.com:5432/da46762bcmo5k4?user=lqrsdxxaliolxh&password=37e4ad3882a0a64ab75c61c2c3090cf2bcaa4f6e8bf3a43ef35754602b380140\",\"host\":\"localhost\"}
