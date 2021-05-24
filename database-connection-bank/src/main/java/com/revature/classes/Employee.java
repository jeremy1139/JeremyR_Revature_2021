package com.revature.classes;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Employee 
{	
	//method to print options
	private static void printOptions()
	{
		System.out.println("Please select a option");
		System.out.println("(1) Approve/Reject account");
		System.out.println("(2) View customer bank account");
		System.out.println("(3) View log of all transactions");
		System.out.println("(4) Log out");
	}
	private static final Logger LOG = LogManager.getLogger(User.class);
	public static void main(String[] args)
	{
		EmployeeDAOImpl database = new EmployeeDAOImpl();
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to the banking EMPLOYEE client.");
		
		//Employee username and password is obtained here
		System.out.println("Please enter a username");
		String name = scan.nextLine();
		System.out.println("Please enter a password");
		String pass = scan.nextLine();
		
		if(database.login(name, pass))
			System.out.println("Login successful.");
		else
		{
			System.out.println("Login unsuccessful");
			LOG.error("Bad login");
			System.exit(0);
		}
		
		boolean temp = true;
		while(temp)
		{
			printOptions();
			//get user input
			int num = scan.nextInt();
			switch(num)
			{
			case 1:
				database.viewPendingAccount();
				break;
			case 2:
				System.out.println("Please enter a username.");
				scan.nextLine();
				String searchuser = scan.nextLine();
				
				if(database.findUser(searchuser))
				{
					database.printInformation(searchuser);
				}
				else
				{
					LOG.error("User not found");
				}
				break;
			case 3:
				System.out.println("Displaying all log transactions.");
				database.viewLog();
				
				System.out.println("End of log transactions");
				break;
			case 4:
				temp = false;
				System.out.println("Logging out.");
				scan.close();
				break;
			}
		}
		
	}
}






