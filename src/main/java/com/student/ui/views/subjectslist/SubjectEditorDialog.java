package com.student.ui.views.subjectslist;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.student.backend.StudentService;
import com.student.backend.Subject;
import com.student.backend.SubjectService;
import com.student.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class SubjectEditorDialog extends AbstractEditorDialog<Subject> {
    private final TextField subjectNameField = new TextField("Subject Name");
    public SubjectEditorDialog(BiConsumer<Subject, Operation> itemSaver,
                               Consumer<Subject> itemDeleter) {
        super("subject", itemSaver, itemDeleter);
        addNameField();
    }

    private void addNameField() {
        getFormLayout().add(subjectNameField);

        getBinder().forField(subjectNameField)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Subject name must contain at least 2 printable characters",
                        3, null))
                .withValidator(
                        name -> SubjectService.getInstance()
                                .findSubjects(name).size() == 0,
                        "Category name must be unique")
                .bind(Subject::getName, Subject::setName);
    }

    @Override
    protected void confirmDelete() {
        int studentCount = StudentService.getInstance()
                .findStudents(getCurrentItem().getName()).size();
        if(studentCount > 0) {
            openConfirmationDialog("Delete subject",
                    "Are you sure you want to delete the “"
                            + getCurrentItem().getName()
                            + "” subject? There are " + studentCount
                            + " students associated with this subject.",
                    "Deleting the subject will mark the associated students as “undefined”. "
                            + "You can edit individual students to select another category.");
        } else {
            doDelete(getCurrentItem());
        }
    }
}
