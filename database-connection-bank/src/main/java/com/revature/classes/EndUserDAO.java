package com.revature.classes;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public interface EndUserDAO 
{
	public void printInformation(String username);
	public Boolean login(String username, String password);
	public Boolean findUser(String username);
}
