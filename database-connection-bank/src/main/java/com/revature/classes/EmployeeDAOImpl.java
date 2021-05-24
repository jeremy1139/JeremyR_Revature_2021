package com.revature.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeDAOImpl implements EndUserDAO {
	public static final String db_username = "postgres";
	public static final String db_password = "password1";
	public static final String db_url = "jdbc:postgresql://localhost:5432/postgres";

	private Connection connection;

	//default constructor
	public EmployeeDAOImpl() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Could not register driver");
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection(db_url, db_username, db_password);
			System.out.println("Connection established: " + connection.isValid(5));
		} catch (SQLException ex) {
			System.out.println("SQL Exception");
			ex.printStackTrace();
		}
	}
	
	//print information on a specified user
	@Override
	public void printInformation(String username) {
		String query = "SELECT username, balance FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			while (output.next()) {
				System.out.println(output.getString("username"));
				System.out.println(output.getInt("balance"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Boolean login(String username, String password) {
		String query = "SELECT * FROM employeeinfo WHERE username = ? AND password = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			x.setString(2, password);
			ResultSet output = x.executeQuery();
			if (output.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean findUser(String username) {
		String query = "SELECT * FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			if (output.next())
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void viewPendingAccount()
	{
		String query = "SELECT * FROM unregisteredaccounts LIMIT 1";
		try
		{
			String username  = "";
			String password = "";
			int amount = 0;
			PreparedStatement x = connection.prepareStatement(query);
			ResultSet output = x.executeQuery();
			while(output.next())
			{
				username = output.getString("username");
				password = output.getString("password");
				amount = output.getInt("startamount");
			}
			System.out.println("Would you like to (1)approve or (2)reject this account?");
			System.out.println("Username: " + username + " Starting amount: " + amount);
			
			Scanner scanner = new Scanner(System.in);
			int input = scanner.nextInt();
			
			if(input == 1)
			{
				String registerQuery = "INSERT INTO customerinfo (username, password, balance) VALUES (?, ?, ?)";
				PreparedStatement y = connection.prepareStatement(registerQuery);
				y.setString(1, username);
				y.setString(2, password);
				y.setInt(3, amount);
				y.executeUpdate();
			}
			String delQuery = "DELETE FROM unregisteredaccounts WHERE username = ?";
			PreparedStatement z = connection.prepareStatement(delQuery);
			z.setString(1, username);
			z.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewLog()
	{
		String query = "SELECT * FROM transactionInfo";
		try
		{
			PreparedStatement x = connection.prepareStatement(query);
			ResultSet output = x.executeQuery();
			while(output.next())
			{
				System.out.println("Username: " + output.getString("username") + " target: " + output.getString("target") 
				+ " Type: " + output.getString("type") + " Amount: " + output.getInt("amount"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}



