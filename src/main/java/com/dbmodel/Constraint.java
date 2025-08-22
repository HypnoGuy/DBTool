package com.dbmodel;

import java.util.List;

public class Constraint {

    // Instance Variables
    private final String constraintName;
    private final String constraintType;
    private final List<ConstraintColumn> constraintColumns;

    // Constructor
    public Constraint(String constraintName, String constraintType, List<ConstraintColumn> constraintColumns) {
        this.constraintName = constraintName;
        this.constraintType = constraintType;
        this.constraintColumns = constraintColumns;
    }

    // Getters and Setters
    public String getConstraintName() { return constraintName; }
    public String getConstraintType() { return constraintType; }
    public List<ConstraintColumn> getConstraintColumns() { return constraintColumns; }

}


