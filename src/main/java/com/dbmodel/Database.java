package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariadb.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class Database {

    // Class Variables and Methods
    private static final List<Database> databases = new ArrayList<>();

    // Instance Variables
    private final Server server;
    private final String name;
    private final String characterSetName;
    private final String collationName;

    private final List<Table> tables = new ArrayList<>();
    private final List<View> views = new ArrayList<>();

    // Constructors
    public Database(Server server, String dbName, String characterSetName, String collationName) {
        this.name = dbName;
        this.characterSetName = characterSetName;
        this.collationName = collationName;
        this.server = server;

        databases.add(this);
        this.server.getDatabases().add(this);
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

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", server.getFullyQualifiedName(), name); }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return name;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Handle the exception, or rethrow as a RuntimeException
            return "{}"; // Or some other default value
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return Objects.equals(server, database.server) && Objects.equals(name, database.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, name);
    }
}
