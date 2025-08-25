package com.dbmodel;

import java.util.ArrayList;
import java.util.List;

public class ConstraintColumn {

    // Class Variables
    private static final List<ConstraintColumn> constraintColumns = new ArrayList<>();

    // Instance Variables
    private final Constraint constraint;
    private final Column column;

    // Constructors
    public ConstraintColumn(Constraint constraint, Column column) {
        this.constraint = constraint;
        this.column = column;

        constraintColumns.add(this);
        constraint.getColumns().add(this);
    }

    // Setters and Getters
    public Column getColumn() { return column; }

    @Override
    public String toString() {
        return column.toString() ;
    }
}
