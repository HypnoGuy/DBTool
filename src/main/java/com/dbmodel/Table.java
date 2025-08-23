package com.dbmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table {

    // Class Variables
    private static final List<Table> tables = new ArrayList<>();

    // Instance Variables
    private Database database;
    private final String name;
    private final String collationName;
    private final List<Column> columns = new ArrayList<>();
    private final List<Constraint> constraints = new ArrayList<>();

    // Constructors
    public Table(Database database, String tableName, String collationName) {
        this.database = database;
        this.name = tableName;
        this.collationName = collationName;

        tables.add(this);
        database.getTables().add(this);
    }

    // Getters and Setters
    public Database getDatabase() { return database; }
    public String getName() { return name; }
    public String getCollationName() { return collationName; }
    public List<Column> getTableColumns() { return columns; }
    public List<Constraint> getConstraints() { return constraints; }

    // Methods
    public byte getMaxColumnNameLength() {

        byte maxColumnNameLength = 0;

        for(Column column : columns) {
            if (column.getColumnName().length() > maxColumnNameLength)
                maxColumnNameLength = (byte) column.getColumnName().length();
        }

        return maxColumnNameLength;
    }

    public String getColumnPrefix() {

        boolean done = false;
        byte charIndex = 1;
        String prefix = "";

        while(!done) {

            prefix = columns.getFirst().getColumnName().substring(0, charIndex);

            for (Column column : columns) {
                if (!column.getColumnName().startsWith(prefix)) {
                    done = true;
                    break;
                }
            }

            if(!done) {
                charIndex++;
                done = (charIndex > getMaxColumnNameLength());
            }
        }

        return prefix.substring(0, prefix.length() - 1);
    }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return "Table{name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(database, table.database) && Objects.equals(name, table.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(database, name);
    }
}
