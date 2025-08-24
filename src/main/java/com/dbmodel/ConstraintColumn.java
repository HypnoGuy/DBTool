package com.dbmodel;

import java.util.ArrayList;
import java.util.List;

public class ConstraintColumn {

    // Class Variables
    private static final List<ConstraintColumn> constraintColumns = new ArrayList<>();

    // Instance Variables
    private final Constraint constraint;
    private final Column column;
    private Column referencedColumn = null;

    // Constructors
    public ConstraintColumn(Constraint constraint, Column column, Column referencedColumn) {
        this.constraint = constraint;
        this.column = column;
        this.referencedColumn = referencedColumn;

        constraintColumns.add(this);
        constraint.getConstraintColumns().add(this);
    }

    public ConstraintColumn(Constraint constraint, Column column) {
        this.constraint = constraint;
        this.column = column;

        constraintColumns.add(this);
        constraint.getConstraintColumns().add(this);
    }

    // Setters and Getters
    public Column getColumn() { return column; }
    public Column getReferencedColumn() { return referencedColumn; }
}
