package com.dbmodel;

import com.mariadb.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {

    // Class Variables
    private static final List<Database> databases = new ArrayList<>();

    // Instance Variables
    private Connection connection;
    private final String name;
    private final String characterSetName;
    private final String collationName;

    private final List<Table> tables = new ArrayList<>();
    private final List<View> views = new ArrayList<>();

    // Constructors
    public Database(Connection connection, String dbName, String characterSetName, String collationName) {
        this.name = dbName;
        this.characterSetName = characterSetName;
        this.collationName = collationName;
        this.connection = connection;

        databases.add(this);
        connection.getServer().getDatabases().add(this);
    }

    // Setters and Getters
    public String getName() { return name; }
    public String getCharacterSetName() { return characterSetName; }
    public String getCollationName() { return collationName; }

    public List<Table> getTables() { return tables; }
    public List<View> getViews() { return views; }

    // Methods
    public boolean hasTable(String tableName) {
        boolean tableFound = false;

        for (Table table : tables) {
            if (table.getName().equals(tableName)) {
                tableFound = true;
                break;
            }
        }

        return tableFound;
    }

    public Table getTable(String tableName) {
        Table foundTable = null;

        for (Table table : tables ) {
            if (table.getName().equals(tableName)) {
                foundTable = table;
                break;
            }
        }

        return foundTable;
    }

    public View getView(String viewName) {
        View foundView = null;

        for (View view : views ) {
            if (view.getName().equals(viewName)) {
                foundView = view;
                break;
            }
        }

        return foundView;
    }
    
    // toString, equals and hashCode
    @Override
    public String toString() {
        return "Database{name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return Objects.equals(connection, database.connection) && Objects.equals(name, database.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection, name);
    }
}
