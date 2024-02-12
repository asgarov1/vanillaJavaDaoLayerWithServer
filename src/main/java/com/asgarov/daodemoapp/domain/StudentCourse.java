package com.asgarov.daodemoapp.domain;

public class StudentCourse {
    private Integer studentCourseId;
    private Integer studentId;
    private Integer courseId;

    public StudentCourse() {
    }

    public StudentCourse(Integer studentCourseId, Integer studentId, Integer courseId) {
        this.studentCourseId = studentCourseId;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studendId) {
        this.studentId = studendId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }
}
