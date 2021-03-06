package services;

import java.sql.*;

public class DBConnection {
    public static Connection connection;
    private static String connectionURL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Quanlythuvien;";
    private static String user = "sa";
    private static String password = "";

    public static Connection getConnection() throws SQLException{
        try {
            connection = DriverManager.getConnection(connectionURL, user, password);
        }
        catch(SQLException e){
            System.out.println("Connect to database failed, some errors occurred!");
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection(String hostName, String userName, String password, String dbName) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlserver://" + hostName + ";databaseName=" + dbName + ";");
        System.out.println("Database successfully connected");

        return connection;
    }
    public static ResultSet getData(String stringSQL, Connection conn) throws SQLException {
        ResultSet rs = null;
        Statement st = conn.createStatement();
        rs = st.executeQuery(stringSQL);
        return rs;
    }
}