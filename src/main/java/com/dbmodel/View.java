package com.dbmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class View {

    // Class Variables
    private static final List<View> views = new ArrayList<>();

    // Instance Variables
    private Database database;
    private final String name;
    private final List<Column> columns = new ArrayList<>();
    
    // Constructors
    public View(Database database, String name) {
        this.database = database;
        this.name = name;

        views.add(this);
        database.getViews().add(this);
    }

    // Getters and Setters
    public List<View> getViews() { return views; }
    public String getName() { return name; }
    public List<Column> getViewColumns() { return columns; }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return "View{name='" + name + "'}";
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
