package com.student.ui.views.studentslist;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.student.backend.ClassId;
import com.student.backend.ClassIdService;
import com.student.backend.Student;
import com.student.backend.Subject;
import com.student.backend.SubjectService;
import com.student.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class StudentEditorDialog extends AbstractEditorDialog<Student> {
    private transient SubjectService subjectService = SubjectService.getInstance();
    private transient ClassIdService classIdService = ClassIdService.getInstance();

    private ComboBox<Subject> subjectComboBox = new ComboBox<>();
    private ComboBox<ClassId> classIdComboBox = new ComboBox<>();
    private ComboBox<String> gradeBox = new ComboBox<>();
    private DatePicker dateOfBirth = new DatePicker();
    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField studentId = new TextField();

    public StudentEditorDialog(BiConsumer<Student, Operation> saveHandler,
                               Consumer<Student> deleteHandler) {
        super("student", saveHandler, deleteHandler);
        createFirstNameField();
        createLastNameField();
        createDatePicker();
        createStudentIdField();
        createClassIdBox();
        createSubjectBox();
        createGradeBox();
    }

    private void createLastNameField() {
        lastName.setLabel("Last Name");
        lastName.setRequired(true);
        getFormLayout().add(lastName);
        getBinder().forField(lastName)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Last Name should contain at least 3 printable character",
                        3, null))
                .bind(Student::getLastName, Student::setLastName);
    }

    private void createFirstNameField() {
        firstName.setLabel("First Name");
        firstName.setRequired(true);
        getFormLayout().add(firstName);
        getBinder().forField(firstName)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "First Name should contain at least 3 printable characters.",
                        3, null))
                .bind(Student::getName, Student::setName);
    }


    private void createGradeBox() {
        gradeBox.setLabel("Grade");
        gradeBox.setRequired(true);
        gradeBox.setAllowCustomValue(false);
        gradeBox.setItems("1", "2", "3", "4", "5");
        getFormLayout().add(gradeBox);
        getBinder().forField(gradeBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The grade should be a number"))
                .withValidator(new IntegerRangeValidator(
                        "The grade must be between 1 and 5", 1, 5))
                .bind(Student::getGrade, Student::setGrade);
    }

    private void createStudentIdField() {
        studentId.setLabel("Student Id");
        studentId.setRequired(true);
        studentId.setPattern("[0-9]*");
        studentId.setPreventInvalidInput(true);
        getFormLayout().add(studentId);
        getBinder().forField(studentId)
                .withConverter(new StringToIntegerConverter(0, "Must enter a number"))
                .withValidator(new IntegerRangeValidator(
                        "The student Id should be between 1 and 9999",
                        1, 9999))
                .bind(Student::getStudentId, Student::setStudentId);
    }

    private void createDatePicker() {
        dateOfBirth.setLabel("Date Of Birth");
        dateOfBirth.setRequired(true);
        dateOfBirth.setMax(LocalDate.now());
        dateOfBirth.setMin(LocalDate.of(1, 1, 1));
        dateOfBirth.setValue(LocalDate.of(1999, 3, 31));
        getFormLayout().add(dateOfBirth);

        getBinder().forField(dateOfBirth)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(new DateRangeValidator(
                        "The Date should be neither before Christ nor in the future.",
                        LocalDate.of(1, 1, 1),
                        LocalDate.now()))
                .bind(Student::getDate, Student::setDate);
    }

    private void createClassIdBox() {
        classIdComboBox.setLabel("ClassIds");
        classIdComboBox.setRequired(true);
        classIdComboBox.setItemLabelGenerator(ClassId::getName);
        classIdComboBox.setItems(classIdService.findClassIds(""));
        getFormLayout().add(classIdComboBox);

        getBinder().forField(classIdComboBox)
                .withValidator(Objects::nonNull,
                        "The Class ID should be defined")
                .bind(Student::getClassId, Student::setClassId);
    }


    private void createSubjectBox() {
        subjectComboBox.setLabel("Subject");
        subjectComboBox.setRequired(true);
        subjectComboBox.setItemLabelGenerator(Subject::getName);
        subjectComboBox.setItems(subjectService.findSubjects(""));
        getFormLayout().add(subjectComboBox);

        getBinder().forField(subjectComboBox)
                .withValidator(Objects::nonNull,
                        "The category should be defined.")
                .bind(Student::getSubject, Student::setSubject);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Delete student",
                "Are you sure you want to delete for “"
                        + getCurrentItem().getName() + "“?", "");
    }
}
