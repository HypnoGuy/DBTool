package com.dbmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Constraint {

    // Class Variables
    private static List<Constraint> constraints = new ArrayList<>();

    // Instance Variables
    private final Table constraintTable;
    private Table referencedTable = null;
    private final String name;
    private final String constraintType;
    private final List<ConstraintColumn> constraintColumns = new ArrayList<>();

    // Constructors
    public Constraint(Table constraintTable, Table referencedTable, String name, String constraintType) {
        this.constraintTable = constraintTable;
        this.referencedTable = referencedTable;
        this.name = name;
        this.constraintType = constraintType;

        constraints.add(this);
        constraintTable.getConstraints().add(this);
        if(constraintType.equals("FOREIGN KEY")) referencedTable.getConstraints().add(this);
    }

    public Constraint(Table constraintTable, String name, String constraintType) {
        this.constraintTable = constraintTable;
        this.name = name;
        this.constraintType = constraintType;

        constraints.add(this);
        constraintTable.getConstraints().add(this);
    }


    // Getters and Setters
    public Table getConstraintTable() { return constraintTable; }
    public Table getReferencedTable() { return referencedTable; }
    public String getName() { return name; }
    public String getConstraintType() { return constraintType; }
    public List<ConstraintColumn> getConstraintColumns() { return constraintColumns; }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return "Constraint{" +
                "name='" + name + "'" +
                ",type='" + constraintType + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}


