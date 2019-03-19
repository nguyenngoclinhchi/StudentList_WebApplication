package com.student.backend;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SubjectService {
    private static class SingletonHolder {
        static final SubjectService INSTANCE = createDemoSubjectService();

        private SingletonHolder() {
        }

        private static SubjectService createDemoSubjectService() {
            SubjectService subjectService = new SubjectService();
            Set<String> subjectNames = new LinkedHashSet<>(
                    StaticData.SUBJECTS.values());
            subjectNames.forEach(name -> {
                Subject subject = subjectService
                        .doSaveSubject(new Subject(name));
                if (StaticData.UNDEFINED.equals(name)) {
                    subjectService.undefinedSubjectId.set(subject.getId());
                }
            });
            return subjectService;
        }
    }

    private Map<Long, Subject> subjects = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);
    private AtomicLong undefinedSubjectId = new AtomicLong(-1);

    public static SubjectService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Subject getUndefineSubject() {
        return subjects.get(undefinedSubjectId.get());
    }

    public List<Subject> findSubjects(String filter) {
        String normalisedFilter = filter.toLowerCase();
        return subjects.values().stream().filter(
                c -> c.getName().toLowerCase().contains(normalisedFilter))
                .map(Subject::new)
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
                .collect(Collectors.toList());
    }

    public Optional<Subject> findSubjectByName(String name) {
        List<Subject> subjectsMatching = findSubjects(name);
        if(subjectsMatching.isEmpty()) {
            return Optional.empty();
        }
        if(subjectsMatching.size() > 1) {
            throw new IllegalStateException("Subject " + name + " is ambiguous");
        }
        return Optional.of(subjectsMatching.get(0));
    }

    public Subject findSubjectOrThrow(String name) {
        return findSubjectByName(name)
                .orElseThrow(() -> new IllegalStateException(
                        "Subject " + name + " does not exist."));
    }

    public Optional<Object> findSubjectById(Long id) {
        Subject subject = subjects.get(id);
        return Optional.ofNullable(subject);
    }

    public boolean deleteSubject(Subject subject) {
        if (subject.getId() != null
                && undefinedSubjectId.get() == subject.getId().longValue()) {
            throw new IllegalStateException("Undefined subject may not be removed");
        }
        return subjects.remove(subject.getId()) != null;
    }

    public void saveSubject(Subject subject) {
        doSaveSubject(subject);
    }

    private Subject doSaveSubject(Subject subject) {
        Subject entity = subjects.get(subject.getId());
        if (entity == null) {
            entity = new Subject(subject);
            if (subject.getId() == null) {
                entity.setId(nextId.incrementAndGet());
            }
            subjects.put(entity.getId(), entity);
        } else if (undefinedSubjectId.get() == subject.getId()
                && !Objects.equals(entity.getName(), subject.getName())) {
            throw new IllegalArgumentException("Undefined subject may not be renamed");
        } else {
            entity.setName(subject.getName());
        }
        return entity;
    }
}
