package helper;

import java.sql.*;

public class DBUtil {
    static Connection connection;

    public static void setUpDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://0.0.0.0:3307/" + SecretManager.getDatabaseName(), SecretManager.getUserName(), SecretManager.getPassword());
            System.out.println("JDBC connection established with Database");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void referenceDBMethod() throws SQLException {
        setUpDatabaseConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users where phone_number = '+919000000000';");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(", ");
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
        resultSet.close();
        statement.close();
        closeDatabaseConnection();
    }

}
