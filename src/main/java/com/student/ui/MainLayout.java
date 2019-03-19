/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.student.ui;

import com.student.ui.views.studentslist.StudentsList;
import com.student.ui.views.subjectslist.SubjectsList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles/shared-styles.html")
@PWA(name = "StudentList", shortName = "StuList")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends Div
        implements RouterLayout, PageConfigurator {

    public MainLayout() {
        H2 title = new H2("Student List");
        title.addClassName("main-layout__title");

        //Create Link for Student List
        //TODO 1: Change ReviewList to StudentList
        RouterLink students = new RouterLink(null, StudentsList.class);
        students.add(new Icon(VaadinIcon.LIST), new Text("Student List"));
        students.addClassName("main-layout__nav-item");
        // Only show as active for the exact URL, but not for sub paths
        students.setHighlightCondition(HighlightConditions.sameLocation());

        //Create Link for Subject List
        //TODO 2: Change CatagoriesList to Subject List
        RouterLink subjects = new RouterLink(null, SubjectsList.class);
        subjects.add(new Icon(VaadinIcon.ARCHIVES), new Text("Subjects"));
        subjects.addClassName("main-layout__nav-item");

        Div navigation = new Div(students, subjects);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }
}
