package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class View {

    // Instance Variables
    private Database database;
    private final String name;
    private final List<ViewColumn> viewColumns = new ArrayList<>();
    
    // Constructors
    public View(Database database, String name) {
        this.database = database;
        this.name = name;

        database.addView(this);
    }

    // Getters and Setters
    public String getName() { return name; }

    public List<ViewColumn> getColumns() { return viewColumns; }
    protected void addViewColumn(ViewColumn viewColumn) { viewColumns.add(viewColumn); }

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", database.getFullyQualifiedName(), name); }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        View view = (View) o;
        return Objects.equals(database, view.database) && Objects.equals(name, view.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(database, name);
    }
}
