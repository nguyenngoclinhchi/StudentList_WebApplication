package com.student.backend;

import java.io.Serializable;
import java.time.LocalDate;

public class Student implements Serializable {
    private Long id = null;
    private int grade;
    private String firstName;
    private String lastName;
    private String name;
    private LocalDate date;
    private Subject subject;
    private int studentId;
    private ClassId classId;

    public Student() {
        reset();
    }

    public Student(int grade, String firstName, String lastName, LocalDate date,
                   Subject subject, int studentId, ClassId classId) {
        this.grade = grade;
        this.firstName = capitalizeWord(firstName);
        this.lastName = lastName.toUpperCase();
        this.name = firstName + " " + lastName;
        this.date = date;
        this.subject = subject;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Student(int grade, String name, LocalDate date, Subject subject, int studentId,
                   ClassId classId) {
        this.grade = grade;
        this.name = name;
        int index = name.lastIndexOf(" ");
        this.firstName = capitalizeWord(name.substring(0, index));
        this.lastName = name.substring(index + 1).toUpperCase();
        this.date = date;
        this.subject = subject;
        this.studentId = studentId;
        this.classId = classId;
    }

    public Student(Student other) {
        this(other.getGrade(), other.getFirstName(), other.getLastName(), other.getDate(), other.getSubject(), other.getStudentId(), other.getClassId());
        this.id = other.getId();
    }

    private void reset() {
        this.id = null;
        this.grade = 1;
        this.firstName = "";
        this.lastName = "";
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = capitalizeWord(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toUpperCase();
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

    public ClassId getClassId() {
        return classId;
    }

    public void setClassId(ClassId classId) {
        this.classId = classId;
    }

    public static String capitalizeWord(String firstName) {
        firstName = firstName.toLowerCase();
        StringBuilder s = new StringBuilder();
        char ch = ' ';
        for (int i = 0; i < firstName.length(); i++) {
            if (ch == ' ' && firstName.charAt(i) != ' ')
                s.append(Character.toUpperCase(firstName.charAt(i)));
            else
                s.append(firstName.charAt(i));
            ch = firstName.charAt(i);
        }
        return s.toString().trim();
    }
    @Override
    public String toString() {
        return "Student{" + "id=" + getId() + ", grade=" + getGrade() + ", firstName="
                + getName() + ", lastName=" + getLastName() + ", subject="
                + getSubject() + ", date=" + getDate() + ", studentId=" + getStudentId()
                + ", classId=" + getClassId() +  "}";
    }
}
