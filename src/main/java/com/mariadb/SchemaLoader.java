package com.mariadb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
        LoadConstraintColumns();
        LoadForeignKeys();
        LoadForeignKeyColumns();
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

    private static void LoadConstraints()  {

        try {

            String SQLQuery = """
                select *
                from
                    information_schema.TABLE_CONSTRAINTS
                where
                    TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    and CONSTRAINT_TYPE != "FOREIGN_KEY";
                """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String constraintName = results.getString("CONSTRAINT_NAME");
                String schemaName = results.getString("TABLE_SCHEMA");
                String tableName = results.getString("TABLE_NAME");
                String constraintType = results.getString("CONSTRAINT_TYPE");

                Constraint constraint = new Constraint(
                    connection.getServer().getDatabase(schemaName).getTable(tableName),
                    constraintName,
                    constraintType
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void LoadConstraintColumns()  {

        try {
            String SQLQuery = """
                select
                    KEY_COLUMN_USAGE.*
                from
                    information_schema.KEY_COLUMN_USAGE
                    inner join information_schema.TABLE_CONSTRAINTS
                        on TABLE_CONSTRAINTS.TABLE_SCHEMA = KEY_COLUMN_USAGE.TABLE_SCHEMA
                        and TABLE_CONSTRAINTS.CONSTRAINT_NAME = KEY_COLUMN_USAGE.CONSTRAINT_NAME
                where
                    KEY_COLUMN_USAGE.TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    and TABLE_CONSTRAINTS.CONSTRAINT_TYPE != "FOREIGN_KEY"
                order by ORDINAL_POSITION;
                """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String schemaName = results.getString("TABLE_SCHEMA");
                String constraintName = results.getString("CONSTRAINT_NAME");

                String tableName = results.getString("TABLE_NAME");
                String columnName = results.getString("COLUMN_NAME");

                ConstraintColumn constraintColumn  = new ConstraintColumn(
                    connection.getServer().getDatabase(schemaName).getTable(tableName).getConstraint(constraintName),
                    connection.getServer().getDatabase(schemaName).getTable(tableName).getColumn(columnName)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void LoadForeignKeys()  {

        try {

            // Query needs pull referred to  schema.table from KEY_COLUMN_USAGE as
            // MariaDB store this at the column level despite a FK only capable of
            // referring to a single table
            String SQLQuery = """
                select distinct
                    TABLE_CONSTRAINTS.*,
                    KEY_COLUMN_USAGE.REFERENCED_TABLE_SCHEMA,
                    KEY_COLUMN_USAGE.REFERENCED_TABLE_NAME
                from
                    information_schema.TABLE_CONSTRAINTS
                    inner join information_schema.KEY_COLUMN_USAGE
                        on TABLE_CONSTRAINTS.TABLE_SCHEMA = KEY_COLUMN_USAGE.TABLE_SCHEMA
                        and TABLE_CONSTRAINTS.CONSTRAINT_NAME = KEY_COLUMN_USAGE.CONSTRAINT_NAME
                where
                    TABLE_CONSTRAINTS.TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    and  TABLE_CONSTRAINTS.CONSTRAINT_TYPE != "FOREIGN_KEY";
                """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String foreignKeyName = results.getString("CONSTRAINT_NAME");
                String schemaName = results.getString("TABLE_SCHEMA");
                String tableName = results.getString("TABLE_NAME");
                String foreignKeyType = results.getString("CONSTRAINT_TYPE");
                String referencedSchemaName = results.getString("REFERENCED_TABLE_SCHEMA");
                String referencedTableName = results.getString("REFERENCED_TABLE_NAME");

                Database referencedDatabase = null;
                Table referencedTable = null;

                // Check reference for existence and that we have their references objects in our model
                if(referencedSchemaName != null) referencedDatabase = connection.getServer().getDatabase(referencedSchemaName);
                if(referencedDatabase != null) referencedTable = referencedDatabase.getTable(referencedTableName);

                ForeignKey foreignKey;
                if(referencedTable != null)
                    foreignKey = new ForeignKey(
                            connection.getServer().getDatabase(schemaName).getTable(tableName),
                            connection.getServer().getDatabase(referencedSchemaName).getTable(referencedTableName),
                            foreignKeyName
                    );

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void LoadForeignKeyColumns()  {

        try {
            String SQLQuery = """
                    select *
                    from information_schema.KEY_COLUMN_USAGE
                    where TABLE_SCHEMA not in ('performance_schema', 'information_schema')
                    order by ORDINAL_POSITION;
                    """;

            ResultSet results = connection.executeQuery(SQLQuery);
            while(results.next()) {

                String foreignKeyName = results.getString("CONSTRAINT_NAME");

                String schemaName = results.getString("TABLE_SCHEMA");
                String tableName = results.getString("TABLE_NAME");
                String columnName = results.getString("COLUMN_NAME");

                String referencedSchemaName = results.getString("REFERENCED_TABLE_SCHEMA");
                String referencedTableName = results.getString("REFERENCED_TABLE_NAME");
                String referencedColumnName = results.getString("REFERENCED_COLUMN_NAME");

                // Check reference for existence and that we have their references objects in our model
                Database referencedDatabase = null;
                Table referencedTable = null;
                Column referencedColumn = null;

                if(referencedSchemaName != null) referencedDatabase = connection.getServer().getDatabase(referencedSchemaName);
                if(referencedDatabase != null) referencedTable = referencedDatabase.getTable(referencedTableName);
                if(referencedTable != null) referencedColumn = referencedTable.getColumn(referencedColumnName);

                ForeignKeyColumn foreignKeyColumn;

                if(referencedColumn != null)
                    foreignKeyColumn = new ForeignKeyColumn(
                            connection.getServer().getDatabase(schemaName).getTable(tableName).getForeignKey(foreignKeyName),
                            connection.getServer().getDatabase(referencedSchemaName).getTable(referencedTableName).getColumn(referencedColumnName),
                            connection.getServer().getDatabase(schemaName).getTable(tableName).getColumn(columnName)
                    );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}