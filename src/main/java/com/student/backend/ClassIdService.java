package com.student.backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ClassIdService {
    private static class SingletonHolder {
        static final ClassIdService INSTANCE = createDemoClassIdService();

        private SingletonHolder() {

        }

        private static ClassIdService createDemoClassIdService() {
            ClassIdService classIdService = new ClassIdService();
            StaticData.ElementList studentList = new StaticData.ElementList();
            List<StaticData.Element> studentElementList = studentList.getSampleStudentList();
            Set<String> className = new HashSet<>();
            for (StaticData.Element classIdElement: studentElementList) {
                className.add(classIdElement.getClassName());
            }
            for (String name : className) {
                ClassId classId = classIdService.doSaveClassId(new ClassId(name));
                if (StaticData.UNDEFINED.equals(name)) {
                    classIdService.undefinedClassId.set(classId.getId());
                }
            }
            return classIdService;
        }
    }

    private Map<Long, ClassId> classIds = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);
    private AtomicLong undefinedClassId = new AtomicLong(-100);

    public static ClassIdService getInstance() {
        return ClassIdService.SingletonHolder.INSTANCE;
    }

    public ClassId getUndefineClassId() {
        return classIds.get(undefinedClassId.get());
    }

    public List<ClassId> findClassIds(String filter) {
        String normalisedFilter = filter.toLowerCase();
        return classIds.values().stream().filter(
                c -> c.getName().toLowerCase().contains(normalisedFilter))
                .map(ClassId::new)
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
                .collect(Collectors.toList());
    }

    public Optional<ClassId> findClassIdByName(String name) {
        List<ClassId> classIdsMatching = findClassIds(name);
        if(classIdsMatching.isEmpty()) {
            return Optional.empty();
        }
        if(classIdsMatching.size() > 1) {
            throw new IllegalStateException("ClassId " + name + " is ambiguous");
        }
        return Optional.of(classIdsMatching.get(0));
    }

    public ClassId findClassIdOrThrow(String name) {
        return findClassIdByName(name)
                .orElseThrow(() -> new IllegalStateException(
                        "ClassId " + name + " does not exist."));
    }

    public Optional<Object> findClassIdById(Long id) {
        ClassId classId = classIds.get(id);
        return Optional.ofNullable(classId);
    }

    public boolean deleteClassId(ClassId classId) {
        if (classId.getId() != null
                && undefinedClassId.get() == classId.getId().longValue()) {
            throw new IllegalStateException("Undefined class Id may not be removed");
        }
        return classIds.remove(classId.getId()) != null;
    }

    public void saveClassId(ClassId classId) {
        doSaveClassId(classId);
    }

    private ClassId doSaveClassId(ClassId classId) {
        ClassId entity = classIds.get(classId.getId());
        if (entity == null) {
            entity = new ClassId(classId);
            if (classId.getId() == null) {
                entity.setId(nextId.incrementAndGet());
            }
            classIds.put(entity.getId(), entity);
        } else if (undefinedClassId.get() == classId.getId()
                && !Objects.equals(entity.getName(), classId.getName())) {
            throw new IllegalArgumentException("Undefined classId may not be renamed");
        } else {
            entity.setName(classId.getName());
        }
        return entity;
    }
}
