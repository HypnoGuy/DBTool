package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariadb.Connection;
import com.mariadb.SchemaLoader;
import com.utilities.JSONMapper;
import com.utilities.TextFileWriter;

public class Main {

    public static void main(String[] args) {

    Connection connection = new Connection();
        connection.setHostName("jendagistaging.directrouter.com");
        connection.setUserName("tf8ef4rk_dev_DBOUser");
        connection.setUserPasswordEncrypted("ENC(J8WjiCcdUwrMfkxxiV/6UnNOX93ib133oHGjOmzxtzqU1A0V2wnDa5oMfL/fMj30CsZvc6kmzL6dOG4FCOxfvA==)");
        SchemaLoader.LoadSchemaFromConnection(connection);

        String rawJSON = connection.getServer().export(new JSONMapper());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(rawJSON);
            String prettyJSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            TextFileWriter textFileWriter = new TextFileWriter("schema.json");
            textFileWriter.WriteAll(prettyJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



    }
}
