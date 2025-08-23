package com.dbmodel;

public class ConstraintColumn {

    private final Column column;
    private final Column referencedColumn;

    // Constructor
    public ConstraintColumn(Column column, Column referencedColumn) {
        this.column = column;
        this.referencedColumn = referencedColumn;
    }

    // Setters and Getters
    public Column getColumn() { return column; }
    public Column getReferencedColumn() { return referencedColumn; }
}
