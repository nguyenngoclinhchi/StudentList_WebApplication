package com.student.ui.views.classidslist;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.student.backend.ClassId;
import com.student.backend.ClassIdService;
import com.student.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class ClassIdEditorDialog extends AbstractEditorDialog<ClassId> {
    private final TextField classIdNameField = new TextField("ClassId Name");
    public ClassIdEditorDialog (BiConsumer<ClassId, Operation> itemSaver,
                               Consumer<ClassId> itemDeleter) {
        super("classId", itemSaver, itemDeleter);
        addNameField();
    }

    private void addNameField() {
        getFormLayout().add(classIdNameField);

        getBinder().forField(classIdNameField)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "ClassId name must contain at least 2 printable characters",
                        3, null))
                .withValidator(
                        name -> ClassIdService.getInstance()
                                .findClassIds(name).size() == 0,
                        "Category name must be unique")
                .bind(ClassId::getName, ClassId::setName);
    }

    @Override
    protected void confirmDelete() {
        int classIdCount = ClassIdService.getInstance()
                .findClassIds(getCurrentItem().getName()).size();
        if(classIdCount > 0) {
            openConfirmationDialog("Delete classId",
                    "Are you sure you want to delete the “"
                            + getCurrentItem().getName()
                            + "” classId? There are " + classIdCount
                            + " students associated with this classId.",
                    "Deleting the classId will mark the associated students as “undefined”. "
                            + "You can edit individual students to select another category.");
        } else {
            doDelete(getCurrentItem());
        }
    }
}
