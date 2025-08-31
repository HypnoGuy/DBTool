package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")

public class ViewColumn {

    // Instance Variables
    private final View view;
    private final String name;

        // Constructor
        public ViewColumn(
            View view,
            String name
    ) {
        this.view = view;
        this.name = name;

        view.addViewColumn(this);
    }

    // Getters and Setters
    public View getView() { return view; }
    public String getName() { return name; }

    // FQ Name
    public String getFullyQualifiedName() {
        return String.join(".", view.getFullyQualifiedName(), name);
    }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ViewColumn that = (ViewColumn) o;
        return Objects.equals(view, that.view) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(view, name);
    }
}

