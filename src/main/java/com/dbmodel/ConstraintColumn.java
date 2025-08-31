package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class ConstraintColumn {

    // Instance Variables
    private final Constraint constraint;
    private final TableColumn tableColumn;

    // Constructors
    public ConstraintColumn(Constraint constraint, TableColumn tableColumn) {
        this.constraint = constraint;
        this.tableColumn = tableColumn;

        constraint.addColumn(this);
    }

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", constraint.getFullyQualifiedName(), tableColumn.getFullyQualifiedName()); }

    // Setters and Getters
    public TableColumn getColumn() { return tableColumn; }

    @Override
    public String toString() {
        return tableColumn.toString() ;
    }
}
