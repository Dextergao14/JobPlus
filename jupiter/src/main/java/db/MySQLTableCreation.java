package db;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as java app to reset the database.
	public static void main(String[] args) {
		try {
			// step1 connect to MySQL
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			} 
			
			// step2 drop tables in case they exist
			Statement statement = conn.createStatement();
			
			String sqlQuery = "DROP TABLE IF EXISTS keywords";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "DROP TABLE IF EXISTS history";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "DROP TABLE IF EXISTS items";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "CREATE TABLE items ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "url VARCHAR(255),"
					+ "PRIMARY KEY (item_id)"
					+ ")";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255),"
					+ "last_name VARCHAR(255),"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "CREATE TABLE keywords ("
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "keyword VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (item_id, keyword),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sqlQuery);
			
			sqlQuery = "CREATE TABLE history ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "item_id VARCHAR(255) NOT NULL,"
					+ "last_favor_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (user_id, item_id),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
					+ "FOREIGN KEY (item_id) REFERENCES items(item_id)"
					+ ")";
			statement.executeUpdate(sqlQuery);
			
			// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
			sqlQuery = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
			statement.executeUpdate(sqlQuery);
			
			conn.close();
			System.out.println("Import done successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
