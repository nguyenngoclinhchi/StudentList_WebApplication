package com.student.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.student.ui.encoders.LocalDateToStringEncoder;

public class StudentService {
    private static class SingletonHolder {
        static final StudentService INSTANCE = createDemoStudentService();

        private SingletonHolder() {
        }

        private static StudentService createDemoStudentService() {
            final StudentService studentService = new StudentService();
            Random r = new Random();
            int studentCount = 40 + r.nextInt(10);
            List<Map.Entry<String, String>> students = new ArrayList<>(StaticData.SUBJECTS.entrySet());
            for (int i = 0; i < studentCount; i++) {
                Student student = new Student();
                Map.Entry<String, String> sub = students
                        .get(r.nextInt(StaticData.SUBJECTS.size()));
                Subject subject = SubjectService.getInstance().findSubjectOrThrow(sub.getValue());
                //Subject subject = new Subject(sub.getValue());

                student.setName(sub.getKey());
                LocalDate testDate = getRandomDate();
                student.setDate(testDate);
                //Grade: 1 --> 5
                student.setGrade(1 + r.nextInt(5));
                student.setSubject(subject);
                //Student Id: 1 --> 9999
                student.setStudentId(1 + r.nextInt(9999));
                studentService.saveStudent(student);
            }
            return studentService;
        }

        private static LocalDate getRandomDate() {
            long minDay = LocalDate.of(1900, 1, 1).toEpochDay();
            long maxDay = LocalDate.now().toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            return LocalDate.ofEpochDay(randomDay);
        }
    }

    private Map<Long, Student> students = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);

    public StudentService() {
    }

    public static StudentService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Student> findStudents(String filter) {
        String normalizedFilter = filter.toLowerCase();
        return students.values().stream().filter(
                students -> filterTextOf(students).contains(normalizedFilter))
                .sorted((s1, s2) -> s2.getName().compareTo(s1.getName()))
                .collect(Collectors.toList());
    }

    private String filterTextOf(Student student) {
        LocalDateToStringEncoder dateToStringEncoder = new LocalDateToStringEncoder();
        String filterableText = Stream.of(
                student.getName(),
                student.getSubject() == null ? StaticData.UNDEFINED
                        : student.getSubject().getName(),
                String.valueOf(student.getGrade()),
                String.valueOf(student.getStudentId()),
                dateToStringEncoder.encode(student.getDate()))
                .collect(Collectors.joining("\t"));
        return filterableText.toLowerCase();
    }

    public boolean deleteStudent(Student student) {
        return students.remove(student.getId()) != null;
    }

    public void saveStudent(Student student) {
        Student entity = students.get(student.getId());
        Subject subject = student.getSubject();

        if(subject != null) {
            subject = (Subject) SubjectService.getInstance()
                    .findSubjectById(subject.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Student(student);
            if (student.getId() == null) {
                entity.setId(nextId.incrementAndGet());
            }
            students.put(entity.getId(), entity);
        } else {
            entity.setGrade(student.getGrade());
            entity.setName(student.getName());
            entity.setDate(student.getDate());
            entity.setStudentId(student.getStudentId());
        }
        entity.setSubject(subject);
    }
}
