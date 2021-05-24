package com.revature.test.junittests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.classes.BankSystem;
import com.revature.classes.CustomerDAOImpl;
import com.revature.classes.EmployeeDAOImpl;
import com.revature.classes.User;

public class BankTests 
{
	public static final String db_username = "postgres";
	public static final String db_password = "password1";
	public static final String db_url = "jdbc:postgresql://localhost:5432/postgres";
	public Connection connection;
	public CustomerDAOImpl database;
	public EmployeeDAOImpl empDatabase;
	
	@BeforeClass
	public static void beforeClass()
	{
		
	}
	
	@Before
	public void before()
	{
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
		database = new CustomerDAOImpl();
		empDatabase = new EmployeeDAOImpl();
	}
	
	@After
	public void after()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void afterClass()
	{
		
	}
	
	//query for existing user
	@Test
	public void existingUser()
	{
		Boolean x = database.findUser("user1");
		assert(x);
	}
	
	//query for non-existing user
	@Test
	public void nonexistingUser()
	{
		Boolean x = database.findUser("nullUser");
		assert(!x);
	}
	
	//testing for already registered user
	@Test
	public void registerTest()
	{
		Boolean x = database.register("user1", "pass12", 3000);
		assert(!x);
	}
	
	//testing for login
	@Test
	public void login()
	{
		Boolean x = empDatabase.login("emp1", "pass1");
		assert(x);
	}
	
	@Test
	public void badLogin()
	{
		Boolean x = empDatabase.login("emp1", "passNull");
	}
}
