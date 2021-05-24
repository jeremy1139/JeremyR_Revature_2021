package com.revature.classes;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class CustomerDAOImpl implements EndUserDAO {
	public static final String db_username = "postgres";
	public static final String db_password = "password1";
	public static final String db_url = "jdbc:postgresql://localhost:5432/postgres";

	private Connection connection;

	//default constructor
	public CustomerDAOImpl() {
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

	//finds a user in the customerinfo table; returns true if found
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
	
	//same as findUser, but in unregisteredaccounts table
	public Boolean findPendingUser(String username) {
		String query = "SELECT * FROM unregisteredaccounts WHERE username = ?";
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
	
	//creates a table entry in unregistered accounts with account information
	public Boolean register(String username, String password, int amount)
	{
		//check if username exists already
		if(findUser(username) || findPendingUser(username))
			return false;
		
		String query = "INSERT INTO unregisteredaccounts (username, password, startamount) VALUES (?, ?, ?)";
		try
		{
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			x.setString(2, password);
			x.setInt(3, amount);
			x.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	//prints balance of current user logged in
	@Override
	public void printInformation(String username) {
		String query = "SELECT username, balance FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			while (output.next()) {
				System.out.println(output.getInt("balance"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//checks for user/pass in customerinfo and returns true if they match
	@Override
	public Boolean login(String username, String password) {
		String query = "SELECT * FROM customerinfo WHERE username = ? AND password = ?";
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

	//withdraw funds from current user logged in with specified amount
	public Boolean withdraw(String username, int amount) {
		String query = "SELECT balance FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			int balance = 0;
			while (output.next()) {
				balance = output.getInt("balance");
			}
			//checks if the user has enough funds
			if (amount > balance)
				return false;
			balance = balance - amount;
			String updateRow = "UPDATE customerinfo SET balance = ? WHERE username = ?";
			PreparedStatement y = connection.prepareStatement(updateRow);
			y.setInt(1, balance);
			y.setString(2, username);
			y.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//deposit funds into current user logged in with specified amount
	public void deposit(String username, int amount) {
		String query = "SELECT balance FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			int balance = 0;
			while (output.next()) {
				balance = output.getInt("balance");
			}

			balance = balance + amount;
			String updateRow = "UPDATE customerinfo SET balance = ? WHERE username = ?";
			PreparedStatement y = connection.prepareStatement(updateRow);
			y.setInt(1, balance);
			y.setString(2, username);
			y.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//creates a entry in pendingtransfer with a source (current user logged in), target, and amount to transfer
	public Boolean transferRequest(String username, String target, int amount) {
		String query = "SELECT balance FROM customerinfo WHERE username = ?";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			int balance = 0;
			while (output.next()) {
				balance = output.getInt("balance");
			}
			//check if source has enough funds
			if (amount > balance)
				return false;
			String insertQuery = "INSERT INTO pendingtransfer (source, target, amount) VALUES (?, ?, ?)";
			PreparedStatement y = connection.prepareStatement(insertQuery);
			y.setString(1, username);
			y.setString(2, target);
			y.setInt(3, amount);
			y.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//checks if there is a entry in pendingtransfer with username as target
	private Boolean checkTransfer(String username)
	{
		String query = "SELECT * FROM pendingtransfer WHERE target = ?";
		try
		{
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			if(output.next())
				return true;
			return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//accepts 1 transfer if there is a pendingtransfer with username as target
	public int acceptTransfer(String username) {
		if (checkTransfer(username) == false)
			return -1;
		String source = "";
		int amount = 0;
		int id = -1;
		String query = "SELECT * FROM pendingtransfer WHERE target = ? LIMIT 1";
		try
		{
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, username);
			ResultSet output = x.executeQuery();
			while(output.next())
			{
				source = output.getString("source");
				amount = output.getInt("amount");
				id = output.getInt("id");
			}
			//reuse deposit and withdraw to add/subtract funds correctly 
			deposit(username, amount);
			withdraw(source, amount);
			//creates a table entry for the transfer catagory
			createTableEntry(source, username, amount, "Transfer");
			//delete transfer row 
			String delQuery = "DELETE FROM pendingtransfer WHERE id = ?";
			PreparedStatement y = connection.prepareStatement(delQuery);
			y.setInt(1, id);
			y.executeUpdate();
			return amount;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	//inserts rows into transactioninfo to keep a log of all transaction that happened
	public void createTableEntry(String source, String target, int amount, String type) {
		String query = "INSERT INTO transactioninfo (username, amount, target, type) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement x = connection.prepareStatement(query);
			x.setString(1, source);
			x.setInt(2, amount);
			x.setString(3, target);
			x.setString(4, type);
			x.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
