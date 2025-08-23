package main;


import com.dbmodel.Server;
import com.mariadb.Connection;
import com.mariadb.SchemaLoader;

public class Main {

    public static void main(String[] args) {

    Connection connection = new Connection();
        connection.setHostName("jendagistaging.directrouter.com");
        connection.setUserName("tf8ef4rk_dev_DBOUser");
        connection.setUserPasswordEncrypted("ENC(J8WjiCcdUwrMfkxxiV/6UnNOX93ib133oHGjOmzxtzqU1A0V2wnDa5oMfL/fMj30CsZvc6kmzL6dOG4FCOxfvA==)");
        SchemaLoader.LoadSchemaFromConnection(connection);

        System.out.println(connection.getServer().getDatabases().getFirst().getTables().getFirst().getColumnPrefix());

    }
}
