package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariadb.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class Server {

    // Class Variables and Methods
    private static final List<Server> servers = new ArrayList<>();

    // Instance Variables
    private HashMap<String,String> globalVariables = new HashMap<String,String>();
    private final Connection connection;
    private final List<Database> databases = new ArrayList<>();

    // Constructor
    public Server(Connection connection, HashMap<String,String> globalVariables) {
        this.connection = connection;
        this.globalVariables = globalVariables;
        Server.servers.add(this);
    }

    // Getters and Setters
    public String getHostName() { return globalVariables.get("HOSTNAME"); }
    public String getProductName() { return globalVariables.get("VERSION_COMMENT"); }
    public String getProductVersion() { return globalVariables.get("VERSION"); }

    public Connection getConnection() { return connection; }
    public List<Database> getDatabases() { return databases; }

    // Methods
    public Database getDatabase(String DatabaseName) {
        Database foundDatabase = null;

        for (Database database : databases) {
            if (database.getName().equals(DatabaseName)) {
                foundDatabase = database;
                break;
            }
        }

        return foundDatabase;
    }
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Handle the exception, or rethrow as a RuntimeException
            e.printStackTrace();
            return "{}"; // Or some other default value
        }
    }

    // FQ Name
    public String getFullyQualifiedName() { return getHostName();}

    // toString, equals and hashCode
    @Override
    public String toString() {
        return getHostName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(getHostName(), server.getHostName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getHostName());
    }



}
