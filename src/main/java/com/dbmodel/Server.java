package com.dbmodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.mariadb.Connection;
import com.utilities.MapperInterface;

import java.util.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class Server {

    // Class Variables and Methods
    private static final List<Server> servers = new ArrayList<>();

    // Instance Variables
    private HashMap<String,String> globalVariables = new HashMap<String,String>();
    private final Connection connection;
    private final Map<String,Database> databases = new HashMap<>();

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

    public List<Database> getDatabases() { return (new ArrayList<>(databases.values())); }
    protected void addDatabase(Database database) { databases.put(database.getName(), database); }
    public Database getDatabase(String DatabaseName) {  return databases.get(DatabaseName); }

    public String export(MapperInterface mapper) { return mapper.map(this) ;}

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
