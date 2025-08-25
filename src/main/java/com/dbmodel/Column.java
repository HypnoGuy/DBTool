package com.dbmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Column {

    // Class variables
    private static List<Column> columns = new ArrayList<Column>();

    // Instance Variables
    private final Table table;
    private final View view;
    private final String name;
    private final String columnType;
    private final String dataType;
    private final Long characterMaximumLength; // For string types
    private final Integer numericPrecision;    // For numeric types
    private final Integer numericScale;        // For numeric types
    private final boolean isNullable;
    private final boolean isAutoIncrement;
    private final String columnDefault;
    private final String characterSetName;
    private final String collationName;

        // Constructor
    public Column(
            Table table,
            View view,
            String name,

            String columnType,
            String dataType,

            Long characterMaximumLength,
            Integer numericPrecision,
            Integer numericScale,

            boolean isNullable,
            boolean isAutoIncrement,

            String columnDefault,
            String characterSetName,
            String collationName
    ) {
        this.table = table;
        this.view = view;
        this.name = name;

        this.columnType = columnType;
        this.dataType = dataType;

        this.characterMaximumLength = characterMaximumLength;
        this.numericPrecision = numericPrecision;
        this.numericScale = numericScale;

        this.isNullable = isNullable;
        this.isAutoIncrement = isAutoIncrement;

        this.columnDefault = columnDefault;
        this.characterSetName = characterSetName;
        this.collationName = collationName;

        columns.add(this);

        if(view == null)
            table.getColumns().add(this);
        else
            view.getViewColumns().add(this);
    }

    // Getters and Setters
    public Table getTable() { return table; }
    public View getView() { return view; }
    public String getName() { return name; }
    public String getColumnType() { return columnType; }
    public String getDataType() { return dataType; }
    public Long getCharacterMaximumLength() { return characterMaximumLength; }
    public Integer getNumericPrecision() { return numericPrecision; }
    public final Integer getNumericScale() { return numericScale ; }
    public final boolean isNullable() { return isNullable ; }
    public final boolean isAutoIncrement() { return isAutoIncrement ; }
    public String getColumnDefault() { return columnDefault; }
    public String getCharacterSetName() { return characterSetName; }
    public String getCollationName() { return collationName ; }

    // toString, equals and hashCode

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Column that = (Column) o;
        return Objects.equals(table, that.table) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, name);
    }
}

