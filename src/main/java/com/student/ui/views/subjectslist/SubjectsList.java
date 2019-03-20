package com.student.ui.views.subjectslist;

import java.util.List;

import com.student.backend.Student;
import com.student.backend.StudentService;
import com.student.backend.Subject;
import com.student.backend.SubjectService;
import com.student.ui.MainLayout;
import com.student.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "subjects", layout = MainLayout.class)
@PageTitle("Subjects List")
public class SubjectsList extends VerticalLayout {
    private final TextField searchField = new TextField("", "Search Subjects");
    private final H2 header = new H2("Subjects");
    private final Grid<Subject> grid = new Grid<>();

    private final SubjectEditorDialog form = new SubjectEditorDialog(
            this::saveSubject, this::deleteSubject);

    public SubjectsList() {
        initView();

        addSearchBar();
        addContent();

        updateView();
    }

    private void saveSubject(Subject subject,
                              AbstractEditorDialog.Operation operation) {
        SubjectService.getInstance().saveSubject(subject);

        Notification.show(
                "Subject successfully " + operation.getNameInText() + "ed.",
                3000, Notification.Position.BOTTOM_CENTER);
        updateView();
    }

    private void deleteSubject(Subject subject) {
        List<Student> subjectsInCategory = StudentService.getInstance()
                .findStudents(subject.getName());

        subjectsInCategory.forEach(student -> {
            student.setSubject(
                    SubjectService.getInstance().getUndefineSubject());
            StudentService.getInstance().saveStudent(student);
        });
        SubjectService.getInstance().deleteSubject(subject);

        Notification.show("Subject successfully deleted.", 3000,
                Notification.Position.BOTTOM_CENTER);
        updateView();
    }

    private void updateView() {
        List<Subject> subjects = SubjectService.getInstance()
                .findSubjects(searchField.getValue());
        grid.setItems(subjects);

        if(searchField.getValue().length() > 0) {
            header.setText("Search for “" + searchField.getValue() + "”");
        } else {
            header.setText("Subjects");
        }
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

        grid.addColumn(Subject::getName).setHeader("Subject name").setWidth("8em")
                .setResizable(true);
        grid.addColumn(this::getNumberOfStudentCount).setHeader("Number of Students ")
                .setWidth("6em");

        grid.addColumn(new ComponentRenderer<>(this::createEditButton))
                .setFlexGrow(0);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        container.add(grid);
        add(container);
    }

    private Button createEditButton(Subject subject) {
        Button edit = new Button("Edit", event -> form.open(subject,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("student__edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if (SubjectService.getInstance().getUndefineSubject().getId()
                .equals(subject.getId())) {
            edit.setEnabled(false);
        }
        return edit;
    }

    private String getNumberOfStudentCount(Subject subject) {
        List<Student> subjectList = StudentService.getInstance()
                        .findStudents(subject.getName());
        //int sum = subjectList.stream().mapToInt(Student::getStudentId).sum();
        int num = subjectList.size();
        return Integer.toString(num);
    }

    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        Button newButton = new Button("New Subject", new Icon("lumo", "plus"));
        newButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> form.open(new Subject(), AbstractEditorDialog.Operation.ADD));
        newButton.addClickShortcut(Key.of("+"));
        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    private void initView() {
        addClassName("subjects-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }
}
