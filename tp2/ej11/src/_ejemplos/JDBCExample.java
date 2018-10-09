package _ejemplos;

import java.sql.*;
 
public class JDBCExample {
 
	public static void main(String[] argv) {
 
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		Connection connection = null;
 
		try {
 
            //"jdbc:postgresql://127.0.0.1:5432/sitio1"
            String connString = String.format(
                    "jdbc:postgresql://%s:%d/%s",
                    "127.0.0.1",
                    5550,
                    "banco");
            String user = "admin";
            String password = "admin";

            connection = DriverManager.getConnection(
                    connString,
                    user,
                    password);

            Statement stmt = connection.createStatement();
            ResultSet rows = stmt.executeQuery("SELECT * FROM cuentas");
            System.out.println("EJECTUANDO....");

            while(rows.next()) {
                System.out.println(String.format(
                            "[%d] TITULAR: %s",
                            rows.getRow(),
                            rows.getString("titular")));
            }
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
 
}
