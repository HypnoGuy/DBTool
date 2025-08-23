package com.mariadb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbmodel.*;

public  class SchemaLoader {

    private static Connection connection ;

    public static void LoadSchemaFromConnection(Connection connection) {
        SchemaLoader.connection = connection;
        LoadServer();
        LoadDatabases();
        LoadTablesAndViews();
        LoadColumns();
        LoadConstraints();
    }

    public static void LoadServer() {

        HashMap<String,String> globalVariables = new HashMap<String,String>();

        try {
            ResultSet results = connection.executeQuery("select * from information_schema.GLOBAL_VARIABLES;");
            while (results.next()) {
                globalVariables.put(results.getString("VARIABLE_NAME"), results.getString("VARIABLE_VALUE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setServer(new Server(connection, globalVariables));
    }

    private static void LoadDatabases()  {

        try {
            String SQLQuery = """
                select *
                from information_schema.SCHEMATA
                where SCHEMA_NAME not in ('performance_schema', 'information_schema');
                """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {
                Database database = new Database(
                        connection,
                        results.getString("SCHEMA_NAME"),
                        results.getString("DEFAULT_CHARACTER_SET_NAME"),
                        results.getString("DEFAULT_COLLATION_NAME")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void LoadTablesAndViews()  {

        String SQLQuery = """
                    select *
                    from information_schema.TABLES
                    where TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    order by TABLE_NAME
                    """;

        try {
            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String schemaName = results.getString("TABLE_SCHEMA");
                String tableType = results.getString("TABLE_TYPE");

                if (! (schemaName.equals("performance_schema") || (schemaName.equals("information_schema")))) {

                    if(tableType.equals("BASE TABLE")) {
                        Table table = new Table(
                                connection.getServer().getDatabase(schemaName),
                                results.getString("TABLE_NAME"),
                                results.getString("TABLE_COLLATION")
                        );
                    } else if(tableType.equals("VIEW")) {
                        View view = new View(
                                connection.getServer().getDatabase(schemaName),
                                results.getString("TABLE_NAME")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void LoadColumns()  {

        try {
            String SQLQuery = """
                    select
                        TABLES.TABLE_TYPE,
                        COLUMNS.*
                    from
                        information_schema.TABLES
                        inner join information_schema.COLUMNS
                            on COLUMNS.TABLE_SCHEMA = TABLES.TABLE_SCHEMA
                            and COLUMNS.TABLE_NAME = TABLES.TABLE_NAME
                    where TABLES.TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    order by TABLES.TABLE_SCHEMA, TABLES.TABLE_NAME, COLUMNS.ORDINAL_POSITION
                    """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String schemaName = results.getString("TABLE_SCHEMA");
                String name = results.getString("TABLE_NAME");
                String tableType = results.getString("TABLE_TYPE");

                Column column = new Column(
                        connection.getServer().getDatabase(schemaName).getTable(name),
                        connection.getServer().getDatabase(schemaName).getView(name),
                        results.getString("COLUMN_NAME"),
                        results.getString("COLUMN_TYPE"),
                        results.getString("DATA_TYPE"),
                        results.getLong("CHARACTER_MAXIMUM_LENGTH"),
                        results.getInt("NUMERIC_PRECISION"),
                        results.getInt("NUMERIC_SCALE"),
                        (results.getString("IS_NULLABLE").equals("YES")),
                        (results.getString("EXTRA").equals("auto_increment")),
                        results.getString("COLUMN_DEFAULT"),
                        results.getString("CHARACTER_SET_NAME"),
                        results.getString("COLLATION_NAME")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Constraint> LoadConstraints()  {

        List<Constraint> Constraints = new ArrayList<>();

        try {
            String SQLQuery = """
                    select *
                    from information_schema.TABLE_CONSTRAINTS;
                    """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String constraintName = results.getString("CONSTRAINT_NAME");
                String schemaName = results.getString("TABLE_SCHEMA");
                String tableName = results.getString("TABLE_NAME");

                Constraint constraint = new Constraint(
                        connection.getServer().getDatabase(schemaName).getTable(tableName),
                        constraintName,
                        results.getString("CONSTRAINT_TYPE")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Constraints;
    }

    private static List<ConstraintColumn> LoadConstraintColumns(Connection connection, String schemaName, String constraintName)  {

        List<ConstraintColumn> ConstraintColumns = new ArrayList<>();

        try {
            String SQLQuery = """
                    select *
                    from information_schema.KEY_COLUMN_USAGE
                    where
                        CONSTRAINT_SCHEMA = "[schemaName]"
                        and CONSTRAINT_NAME = "[constraintName]";
                    """;

            SQLQuery = SQLQuery.replace("[schemaName]", schemaName);
            SQLQuery = SQLQuery.replace("[constraintName]", constraintName);

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                /* ConstraintColumn constraintColumn = new ConstraintColumn(
                        ?,
                        ?
                );

                ConstraintColumns.add(constraintColumn);

                */
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ConstraintColumns;
    }
}