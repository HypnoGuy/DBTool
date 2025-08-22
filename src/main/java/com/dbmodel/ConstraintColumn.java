package com.dbmodel;

public class ConstraintColumn {

    private final TableColumn column;
    private final TableColumn referencedColumn;

    // Constructor
    public ConstraintColumn(TableColumn column, TableColumn referencedColumn) {
        this.column = column;
        this.referencedColumn = referencedColumn;
    }

    // Setters and Getters
    public TableColumn getColumn() { return column; }
    public TableColumn getReferencedColumn() { return referencedColumn; }
}
