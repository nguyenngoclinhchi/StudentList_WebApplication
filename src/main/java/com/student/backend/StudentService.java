package com.student.backend;

import static com.student.backend.Student.capitalizeWord;
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
            StaticData.ElementList studentList = new StaticData.ElementList();
            List<StaticData.Element> studentTripleList = studentList.getSampleStudentList();
            int studentCount = r.nextInt(studentTripleList.size());
            for (int i = 0; i < studentCount; i++) {
                Student student = new Student();
                StaticData.Element sub = studentTripleList.get(i);
                Subject subject = SubjectService.getInstance().findSubjectOrThrow(sub.getSubject());
                ClassId classId = ClassIdService.getInstance().findClassIdOrThrow(sub.getClassName());

                student.setFirstName(sub.getFirstName());
                student.setLastName(sub.getLastName());

                LocalDate testDate = getRandomDate();
                student.setDate(testDate);

                //Grade: 1 --> 5
                student.setGrade(1 + r.nextInt(5));
                student.setSubject(subject);

                //Student Id: 1 --> 9999
                student.setStudentId(1 + r.nextInt(9999));

                //Class Id: 1 --> 10
                student.setClassId(classId);
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
                .sorted((s1, s2) -> s2.getFirstName().compareTo(s1.getFirstName()))
                .collect(Collectors.toList());
    }

    private String filterTextOf(Student student) {
        LocalDateToStringEncoder dateToStringEncoder = new LocalDateToStringEncoder();
        String filterableText = String.join("\t",
                student.getFirstName(),
                student.getLastName(),
                String.valueOf(student.getStudentId()),
                dateToStringEncoder.encode(student.getDate()),
                student.getClassId() == null ? StaticData.UNDEFINED : student.getClassId().getName(),
                student.getSubject() == null ? StaticData.UNDEFINED : student.getSubject().getName(),
                String.valueOf(student.getGrade()));
        return filterableText.toLowerCase();
    }

    public boolean deleteStudent(Student student) {
        return students.remove(student.getId()) != null;
    }

    public void saveStudent(Student student) {
        Student entity = students.get(student.getId());
        Subject subject = student.getSubject();
        ClassId classId = student.getClassId();

        if(subject != null) {
            subject = (Subject) SubjectService.getInstance()
                    .findSubjectById(subject.getId()).orElse(null);
        }
        if(classId != null) {
            classId = (ClassId) ClassIdService.getInstance()
                    .findClassIdById(classId.getId()).orElse(null);
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
            entity.setFirstName(student.getFirstName());
            entity.setLastName(student.getLastName());
            entity.setDate(student.getDate());
            entity.setStudentId(student.getStudentId());
        }
        entity.setSubject(subject);
        entity.setClassId(classId);
    }
}
