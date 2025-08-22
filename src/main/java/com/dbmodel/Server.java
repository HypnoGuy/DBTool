package com.dbmodel;

import com.mariadb.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    // Class Variables
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
    public static List<Server> getServers() { return servers; }

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
}
