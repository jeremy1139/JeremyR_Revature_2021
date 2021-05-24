package com.revature.util;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionUtility {
	private static final String CONNECTION_USERNAME = "postgres";
	private static final String CONNECTION_PASSWORD = "password1";
	private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/postgres";

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not register driver");
			e.printStackTrace();
		}

		try {
			Connection connection = DriverManager.getConnection(CONNECTION_URL, CONNECTION_USERNAME, CONNECTION_PASSWORD);
			System.out.println("Connection established: " + connection.isValid(5));
		} catch (SQLException ex) {
			System.out.println("SQL Exception");
			ex.printStackTrace();

		}
	}

}
