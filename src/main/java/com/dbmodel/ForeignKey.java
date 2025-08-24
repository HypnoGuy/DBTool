package com.dbmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForeignKey {

    // Class Variables
    private static List<ForeignKey> constraints = new ArrayList<>();

    // Instance Variables
    private final Table masterTable;
    private Table detailTable = null;
    private final String name;
    private final List<ConstraintColumn> constraintColumns = new ArrayList<>();

    // Constructors
    public ForeignKey(Table masterTable, Table detailTable, String name) {
        this.masterTable = masterTable;
        this.detailTable = detailTable;
        this.name = name;

        constraints.add(this);
        masterTable.getForeignKeys().add(this);
        detailTable.getForeignKeys().add(this);
    }

    // Getters and Setters
    public Table getMasterTable() { return masterTable; }
    public Table getDetailTable() { return detailTable; }
    public String getName() { return name; }
    public List<ConstraintColumn> getConstraintColumns() { return constraintColumns; }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return "ForeignKey{" +
                "name='" + name + "'" +
                "}";
    }

    @Override
    // TODO check this, needs database?
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(name, that.name);
    }

    @Override
    // TODO check this, needs database?
    public int hashCode() {
        return Objects.hashCode(name);
    }
}


