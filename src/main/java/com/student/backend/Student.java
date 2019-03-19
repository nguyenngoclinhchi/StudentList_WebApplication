package com.student.backend;

import java.io.Serializable;
import java.time.LocalDate;

public class Student implements Serializable {
    private Long id = null;
    private int grade;
    private String name;
    private LocalDate date;
    private Subject subject;
    private int studentId;

    public Student() {
        reset();
    }

    public Student(int grade, String name, LocalDate date, Subject subject, int studentId) {
        this.grade = grade;
        this.name = name;
        this.date = date;
        this.subject = subject;
        this.studentId = studentId;
    }

    public Student(Student other) {
        this(other.getGrade(), other.getName(), other.getDate(), other.getSubject(), other.getStudentId());
        this.id = other.getId();
    }

    private void reset() {
        this.id = null;
        this.grade = 1;
        this.name = "";
        this.date = LocalDate.of(1999, 3, 31);
        this.subject = null;
        this.studentId = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + getId() + ", grade=" + getGrade() + ", name=" + getName() + ", subject="
                + getSubject() + ", date=" + getDate() + ", studentId=" + getStudentId() + "}";
    }
}
