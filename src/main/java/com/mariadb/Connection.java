package com.mariadb;

import com.dbmodel.Server;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.utilities.CredentialEncryptor;
import java.sql.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullyQualifiedName")
public class Connection {

    private java.sql.Connection connection;

    private Server server;
    private String hostName;
    private String userName;
    private String userPasswordEncrypted;

    private boolean isConnected;

    // Setters and getters
    public Server getServer() { return server; }
    protected void setServer(Server server) { this.server = server; }

    public void setHostName (String value) { this.hostName = value; }
    public String getHostName() { return this.hostName; }
    public void setUserName (String value) { this.userName = value; }
    public void setUserPasswordEncrypted(String value) { this.userPasswordEncrypted = value; }

    // Methods
    public ResultSet executeQuery(String query) throws SQLException {

        if (!isConnected) connect();

        if (isConnected) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } else {
            return null;
        }
    }

    public ResultSet executeParameterizedQuery(String query, Object... parameters) throws SQLException {
        if (!isConnected) connect();

        if (isConnected) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            return preparedStatement.executeQuery();
        } else {
            return null;
        }
    }

    // Private
    public void connect() throws SQLException {

        String connectionString = "jdbc:mariadb://" + hostName + "?sslMode=required";

        // Decrypt the password before using it
        String decryptedPassword = CredentialEncryptor.decrypt(userPasswordEncrypted);

        connection = DriverManager.getConnection(
                connectionString,
                userName,
                decryptedPassword
        );

        isConnected = true;
    }

    // FQ Name
    public String getFullyQualifiedName() { return String.join(".", this.hostName, this.userName); }

    // toString, equals and hashCode
    @Override
    public String toString() {
        return "Connection{" +
                "hostName='" + hostName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    // TODO hashCode, equals?
}
