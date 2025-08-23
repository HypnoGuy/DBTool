package com.dbmodel;

import java.util.ArrayList;
import java.util.List;

public class Constraint {

    // Class Variables
    private static List<Constraint> constraints = new ArrayList<>();

    // Instance Variables
    private final String constraintName;
    private final String constraintType;
    private final List<ConstraintColumn> constraintColumns = new ArrayList<>();

    // Constructor
    public Constraint(Table table, String constraintName, String constraintType) {
        this.constraintName = constraintName;
        this.constraintType = constraintType;

        constraints.add(this);
        table.getConstraints().add(this);
    }

    // Getters and Setters
    public String getConstraintName() { return constraintName; }
    public String getConstraintType() { return constraintType; }
    public List<ConstraintColumn> getConstraintColumns() { return constraintColumns; }

}


