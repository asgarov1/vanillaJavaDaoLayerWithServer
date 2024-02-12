-- Order of table creation is important!
-- Referenced tables have to be created first

CREATE TABLE IF NOT EXISTS student_group(
                       group_id SERIAL PRIMARY KEY,
                       group_name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS student (
                          student_id SERIAL PRIMARY KEY,
                          group_id INT,
                          first_name VARCHAR(20) NOT NULL,
                          last_name VARCHAR(20) NOT NULL,
                          FOREIGN KEY (group_id) REFERENCES student_group (group_id)
);

CREATE TABLE IF NOT EXISTS course(
                        course_id SERIAL PRIMARY KEY,
                        course_name VARCHAR(30) NOT NULL,
                        course_description VARCHAR(200) NOT NULL
);

create table IF NOT EXISTS student_course
(
    student_course_id SERIAL PRIMARY KEY,
    student_id INT,
    course_id INT,
    CONSTRAINT FK_student FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE,
    CONSTRAINT FK_course FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);