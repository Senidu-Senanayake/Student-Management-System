create database StudentManagementSystemDB;
use StudentManagementSystemDB;

create table tbLogin(
	
	id INT AUTO_INCREMENT PRIMARY KEY,          
    username VARCHAR(50) NOT NULL UNIQUE,       
    password VARCHAR(255) NOT NULL
);
create table tbCourse(

	Course_Name varchar(50) NOT NULL PRIMARY KEY,
    Description varchar(100),
    Degree varchar(100)
);

create table tbStudent (
	
    StudentId varchar(50) NOT NULL PRIMARY KEY,
    Year varchar(50),
    Course varchar(50) NOT NULL,
    First_Name varchar(50),
    Last_Name varchar(50),
    Gender varchar(50),
    Birth_Day date,
    FOREIGN KEY (Course) REFERENCES tbCourse(Course_Name) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE tbGrade (
    Sid VARCHAR(50) NOT NULL,
    Year VARCHAR(50) NOT NULL,
    Course VARCHAR(50) NOT NULL,
    First_Sem DOUBLE,
    Second_Sem DOUBLE,
    PRIMARY KEY (Sid, Course), 
    FOREIGN KEY (Sid) REFERENCES tbStudent(StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (Course) REFERENCES tbCourse(Course_Name) ON DELETE CASCADE ON UPDATE CASCADE
);
ALTER TABLE tbStudent ADD UNIQUE (Year);

insert into tbLogin (username, password) values
("admin","123");

insert into tbCourse value 
("DSE","Software Engineering","BSC Software Engineering");

insert into tbStudent values 
("S001","First Year","DSE","Senidu","Senanayake","Male","2003-06-09"),
("S002","First Year","DNE","Nipuni","Perera","Female","2003-05-25"),
("S003","First Year","DSE","Kash","Silva","Male","2004-11-19"),
("S004","First Year","DNE","Haran","Jayawardhana","Male","2006-03-14");

insert into tbGrade Values 
("S001","First Year","DSE",4.0,3.8);

select * from tbLogin;
