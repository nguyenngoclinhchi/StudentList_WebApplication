package com.student.ui.views.studentslist;

import java.util.List;

import com.student.backend.Student;
import com.student.backend.StudentService;
import com.student.ui.MainLayout;
import com.student.ui.common.AbstractEditorDialog;
import com.student.ui.encoders.LocalDateToStringEncoder;
import com.student.ui.encoders.LongToStringEncoder;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Student List")
@Tag("students-list")
@HtmlImport("frontend://src/views/students-list/students-list.html")
public class StudentsList extends PolymerTemplate<StudentsList.StudentsModel> {

    public interface StudentsModel extends TemplateModel {
        @Encode(value = LongToStringEncoder.class, path = "id")
        @Encode(value = LocalDateToStringEncoder.class, path = "date")
        @Encode(value = LongToStringEncoder.class, path = "subject.id")
        @Encode(value = LongToStringEncoder.class, path = "classId.id")
        void setStudents(List<Student> students);
    }

    @Id("search")
    private TextField searchTextField;
    @Id("newStudent")
    private Button addStudentButton;
    @Id("header")
    private H2 header;

    private StudentEditorDialog studentForm = new StudentEditorDialog(
            this::saveUpdate, this::deleteUpdate);

    public StudentsList() {
        searchTextField.setPlaceholder("Search Students");
        searchTextField.addValueChangeListener(e -> updateList());

        //the level of interaction of the search bar to the data
        //ValueChangeMode.EAGER: High interaction of Search
        //ValueChangeMode.ON_CHANGE: Medium interaction of Search --> have to click Enter to search
        searchTextField.setValueChangeMode(ValueChangeMode.EAGER);

        //The shortcut key is CTL + F
        searchTextField.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        addStudentButton.addClickListener(e -> openForm(new Student(),
                AbstractEditorDialog.Operation.ADD));

        addStudentButton.addClickShortcut(Key.of("+"));

        getElement().setProperty("studentButtonText", "New Student");
        getElement().setProperty("editButtonText", "Edit");

        updateList();
    }

    public void saveUpdate(Student student, AbstractEditorDialog.Operation operation) {
        StudentService.getInstance().saveStudent(student);
        updateList();
        Notification.show(
                "Student successfully " + operation.getNameInText() + "ed.", 3000,
                Notification.Position.BOTTOM_CENTER);
    }

    public void deleteUpdate(Student student) {
        StudentService.getInstance().deleteStudent(student);
        updateList();
        Notification.show("Student successfully deleted.", 3000,
                Notification.Position.BOTTOM_CENTER);
    }

    private void updateList() {
        List<Student> students = StudentService.getInstance().findStudents(searchTextField.getValue());
        if(searchTextField.isEmpty()) {
            header.setText("Students");
            header.add(new Span(students.size() + " in total"));
        } else {
            header.setText("Search for “" + searchTextField.getValue() + "“");
            if(!students.isEmpty()) {
                header.add(new Span(students.size() + " results"));
            }
        }
        getModel().setStudents(students);
    }

    @EventHandler
    private void edit(@ModelItem Student student) {
        openForm(student, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(Student student, AbstractEditorDialog.Operation operation) {
        if (studentForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(studentForm));
        }
        studentForm.open(student, operation);
    }
}
