package com.dbmodel;

import java.util.ArrayList;
import java.util.List;

public class View {

    // Class Variables
    private static final List<View> views = new ArrayList<>();

    // Instance Variables
    private Database database;
    private final String name;

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

}
