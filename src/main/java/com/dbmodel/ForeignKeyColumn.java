package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class ForeignKeyColumn {

    // Class Variables
    private static final List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();

    // Instance Variables
    private final ForeignKey foreignKey;
    private final TableColumn masterTableColumn;
    private TableColumn detailTableColumn = null;

    // Constructors
    public ForeignKeyColumn(ForeignKey foreignKey, TableColumn masterTableColumn, TableColumn detailTableColumn) {
        this.foreignKey = foreignKey;
        this.masterTableColumn = masterTableColumn;
        this.detailTableColumn = detailTableColumn;

        foreignKeyColumns.add(this);
        foreignKey.getColumns().add(this);
    }

    public ForeignKeyColumn(ForeignKey foreignKey, TableColumn tableColumn) {
        this.foreignKey = foreignKey;
        this.masterTableColumn = tableColumn;

        foreignKeyColumns.add(this);
        foreignKey.getColumns().add(this);
    }

    // Setters and Getters
    public TableColumn getMasterColumn() { return masterTableColumn; }
    public TableColumn getDetailColumn() { return detailTableColumn; }

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".",foreignKey.getFullyQualifiedName(), masterTableColumn.getFullyQualifiedName()); }

    // toString, equals and hashCode
    // TODO where are they ?
    @Override
    public String toString() { return masterTableColumn.toString() + " -> " + detailTableColumn.toString() ; }


}
