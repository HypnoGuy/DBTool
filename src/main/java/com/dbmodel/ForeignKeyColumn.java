package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.utilities.MapperInterface;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class ForeignKeyColumn {

    // Instance Variables
    private final ForeignKey foreignKey;
    private final TableColumn masterTableColumn;
    private TableColumn detailTableColumn = null;

    // Constructors
    public ForeignKeyColumn(ForeignKey foreignKey, TableColumn masterTableColumn, TableColumn detailTableColumn) {
        this.foreignKey = foreignKey;
        this.masterTableColumn = masterTableColumn;
        this.detailTableColumn = detailTableColumn;

        foreignKey.addForeignKeyColumn(this);
    }

    public ForeignKeyColumn(ForeignKey foreignKey, TableColumn tableColumn) {
        this.foreignKey = foreignKey;
        this.masterTableColumn = tableColumn;

        foreignKey.addForeignKeyColumn(this);
    }

    // Setters and Getters
    public TableColumn getMasterColumn() { return masterTableColumn; }
    public TableColumn getDetailColumn() { return detailTableColumn; }

    public String export(MapperInterface mapper) { return mapper.map(this) ;}

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".",foreignKey.getFullyQualifiedName(), masterTableColumn.getFullyQualifiedName()); }

    // toString, equals and hashCode
    // TODO where are they ?
    @Override
    public String toString() { return masterTableColumn.toString() + " -> " + detailTableColumn.toString() ; }


}
