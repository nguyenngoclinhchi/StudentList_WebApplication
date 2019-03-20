package com.student.ui.views.classidslist;

import java.util.List;

import com.student.backend.ClassId;
import com.student.backend.ClassIdService;
import com.student.backend.Student;
import com.student.backend.StudentService;
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

@Route(value = "classIds", layout = MainLayout.class)
@PageTitle("ClassIds List")
public class ClassIdsList extends VerticalLayout {
    private final TextField searchField = new TextField("", "Search ClassIds");
    private final H2 header = new H2("ClassIds");
    private final Grid<ClassId> grid = new Grid<>();

    private final ClassIdEditorDialog form = new ClassIdEditorDialog(
            this::saveClassId, this::deleteClassId);

    public ClassIdsList() {
        initView();

        addSearchBar();
        addContent();

        updateView();
    }

    private void saveClassId(ClassId classId,
                             AbstractEditorDialog.Operation operation) {
        ClassIdService.getInstance().saveClassId(classId);

        Notification.show(
                "ClassId successfully " + operation.getNameInText() + "ed.",
                3000, Notification.Position.BOTTOM_CENTER);
        updateView();
    }

    private void deleteClassId(ClassId classId) {
        List<Student> classIdsInCategory = StudentService.getInstance()
                .findStudents(classId.getName());

        classIdsInCategory.forEach(sample -> {
            sample.setClassId(ClassIdService.getInstance().getUndefineClassId());
            StudentService.getInstance().saveStudent(sample);
        });
        ClassIdService.getInstance().deleteClassId(classId);

        Notification.show("ClassId successfully deleted.", 3000,
                Notification.Position.BOTTOM_CENTER);
        updateView();
    }

    private void updateView() {
        List<ClassId> classIds = ClassIdService.getInstance()
                .findClassIds(searchField.getValue());
        grid.setItems(classIds);

        if(searchField.getValue().length() > 0) {
            header.setText("Search for “" + searchField.getValue() + "”");
        } else {
            header.setText("ClassIds");
        }
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

        grid.addColumn(ClassId::getName).setHeader("ClassId name").setWidth("8em")
                .setResizable(true);
        grid.addColumn(this::getNumberOfStudentCount).setHeader("Number of Students ")
                .setWidth("6em");

        grid.addColumn(new ComponentRenderer<>(this::createEditButton))
                .setFlexGrow(0);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        container.add(grid);
        add(container);
    }

    private Button createEditButton(ClassId classId) {
        Button edit = new Button("Edit", event -> form.open(classId,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("student__edit");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if (ClassIdService.getInstance().getUndefineClassId().getId().equals(classId.getId())) {
            edit.setEnabled(false);
        }
        return edit;
    }

    private String getNumberOfStudentCount(ClassId classId) {
        List<Student> studentList = StudentService.getInstance()
                .findStudents(classId.getName());
        //int sum = classId.stream().mapToInt(Student::getStudentId).sum();
        int num = studentList.size();
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

        Button newButton = new Button("New ClassId", new Icon("lumo", "plus"));
        newButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> form.open(new ClassId(), AbstractEditorDialog.Operation.ADD));
        newButton.addClickShortcut(Key.of("+"));
        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    private void initView() {
        addClassName("classIds-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }
}
