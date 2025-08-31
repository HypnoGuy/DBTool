package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.utilities.MapperInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class Table {

    // Instance Variables
    private Database database;
    private final String name;
    private final String collationName;
    private final List<TableColumn> tableColumns = new ArrayList<>();
    private final List<Constraint> constraints = new ArrayList<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();

    // Constructors
    public Table(Database database, String name, String collationName) {
        this.database = database;
        this.name = name;
        this.collationName = collationName;

        database.addTable(this);
    }

    // Getters and Setters
    public Database getDatabase() { return database; }
    public String getName() { return name; }
    public String getCollationName() { return collationName; }

    public List<TableColumn> getColumns() { return tableColumns; }
    protected void addColumn(TableColumn column) { tableColumns.add(column); }
    public TableColumn getColumn(String columnName) {
        TableColumn foundTableColumn = null;

        for (TableColumn TableColumn : tableColumns) {
            if (TableColumn.getName().equals(columnName)) {
                foundTableColumn = TableColumn;
                break;
            }
        }

        return foundTableColumn;
    }

    public List<Constraint> getConstraints() { return constraints; }
    protected void addConstraint(Constraint constraint) { constraints.add(constraint); }
    public Constraint getConstraint(String constraintName) {
        Constraint foundConstraint = null;

        for (Constraint Constraint : constraints ) {
            if (Constraint.getName().equals(constraintName)) {
                foundConstraint = Constraint;
                break;
            }
        }

        return foundConstraint;
    }

    public List<ForeignKey> getForeignKeys() { return foreignKeys; }
    protected void addForeignKey(ForeignKey foreignKey) { foreignKeys.add(foreignKey); }
    public ForeignKey getForeignKey(String foreignKeyName) {
        ForeignKey foundForeignKey = null;

        for (ForeignKey ForeignKey : foreignKeys ) {
            if (ForeignKey.getName().equals(foreignKeyName)) {
                foundForeignKey = ForeignKey;
                break;
            }
        }

        return foundForeignKey;
    }

    public byte getMaxColumnNameLength() {

        byte maxColumnNameLength = 0;

        for(TableColumn tableColumn : tableColumns) {
            if (tableColumn.getName().length() > maxColumnNameLength)
                maxColumnNameLength = (byte) tableColumn.getName().length();
        }

        return maxColumnNameLength;
    }

    public String getColumnPrefix() {

        boolean done = false;
        byte charIndex = 1;
        String prefix = "";

        while(!done) {

            prefix = tableColumns.getFirst().getName().substring(0, charIndex);

            for (TableColumn tableColumn : tableColumns) {
                if (!tableColumn.getName().startsWith(prefix)) {
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

    public String export(MapperInterface mapper) { return mapper.map(this) ;}

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", database.getFullyQualifiedName(), name); }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return name;
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
