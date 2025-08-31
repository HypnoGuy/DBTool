package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class ForeignKey {

    // Instance Variables
    private final Table masterTable;
    private Table detailTable = null;
    private final String name;
    private final List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();

    // Constructors
    public ForeignKey(Table masterTable, Table detailTable, String name) {
        this.masterTable = masterTable;
        this.detailTable = detailTable;
        this.name = name;

        masterTable.addForeignKey(this);
        detailTable.addForeignKey(this);
    }

    // Getters and Setters
    public Table getMasterTable() { return masterTable; }
    public Table getDetailTable() { return detailTable; }
    public String getName() { return name; }

    public List<ForeignKeyColumn> getForeignKeyColumns() { return foreignKeyColumns; }
    protected void addForeignKeyColumn(ForeignKeyColumn foreignKeyColumn) { foreignKeyColumns.add(foreignKeyColumn); }

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", masterTable.getFullyQualifiedName(), name); }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return name;
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


