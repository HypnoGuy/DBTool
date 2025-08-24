package com.dbmodel;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyColumn {

    // Class Variables
    private static final List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();

    // Instance Variables
    private final ForeignKey foreignKey;
    private final Column masterColumn;
    private Column detailColumn = null;

    // Constructors
    public ForeignKeyColumn(ForeignKey foreignKey, Column masterColumn, Column detailColumn) {
        this.foreignKey = foreignKey;
        this.masterColumn = masterColumn;
        this.detailColumn = detailColumn;

        foreignKeyColumns.add(this);
        foreignKey.getColumns().add(this);
    }

    public ForeignKeyColumn(ForeignKey foreignKey, Column column) {
        this.foreignKey = foreignKey;
        this.masterColumn = column;

        foreignKeyColumns.add(this);
        foreignKey.getColumns().add(this);
    }

    // Setters and Getters
    public Column getMasterColumn() { return masterColumn; }
    public Column getDetailColumn() { return detailColumn; }
}
