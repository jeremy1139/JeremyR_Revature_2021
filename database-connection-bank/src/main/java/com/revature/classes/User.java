package com.revature.classes;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User 
{
	
	private static void printOptions()
	{
		System.out.println("Please select a option");
		System.out.println("(1) View Balance");
		System.out.println("(2) Withdraw");
		System.out.println("(3) Deposit");
		System.out.println("(4) Transfer money");
		System.out.println("(5) Accept Transfer");
		System.out.println("(6) Log out");
	}
	
	private static final Logger LOG = LogManager.getLogger(User.class);
	
	public static void main(String[] args)
	{
		//DAO implementation for handling SQL commands
		CustomerDAOImpl database = new CustomerDAOImpl();
		//scanner for user input
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Would you like to (1)register or (2)login?");
		int num = scan.nextInt();
		scan.nextLine();
		
		//register condition
		if(num == 1)
		{
			System.out.println("Please enter a username");
			String name = scan.nextLine();
			System.out.println("Please enter a password");
			String pass = scan.nextLine();
			System.out.println("Please enter a starting amount of funds");
			int amount = scan.nextInt();
			
			//checks if username already exists and creates a request for account creation if username does not exist
			if(database.register(name, pass, amount))
			{
				System.out.println("Request for registeration received. Please login later when request is approved.");
			}
			else
			{
				LOG.error("Unable to register username; Username already exists");
				System.out.println("Username already exists.");
			}
		}
		//login condition
		if(num == 2)
		{
			System.out.println("Please enter a username");
			String name = scan.nextLine();
			System.out.println("Please enter a password");
			String pass = scan.nextLine();

			//query database to login
			if(database.login(name, pass) == false)
			{
				LOG.error("Login failed");
				System.out.println("Invalid login. Exiting.");
				System.out.println("Request for registeration received. Please login later when request is approved.");
				System.exit(1);
			}
			//rejects and exits if wrong

			System.out.println("Login successful.");
			boolean temp = true;
			//while loop so that user can select different options
			while(temp)
			{
				printOptions();
				//get user input
				int x = scan.nextInt();
				switch(x)
				{
				case 1:
					//prints the balance for the current user logged in
					System.out.println("Your current balance is: ");
					database.printInformation(name);
					break;
				case 2:
					//withdraw case
					System.out.println("How much would you like to withdraw?");
					int withdraw = scan.nextInt();
					if(database.withdraw(name, withdraw))
						System.out.println("Withdraw successful");
					else
					{
						LOG.error("Withdraw unsuccessful; Insufficient funds");
						System.out.println("Withdraw unsuccessful");
						break;
					}
					
					//creates log entry of withdraw
					database.createTableEntry(name, name, withdraw, "Withdraw");
					break;
				case 3:
					//deposit case
					System.out.println("How much would you like to deposit?");
					int deposit = scan.nextInt();
					if(deposit < 0)
					{
						LOG.error("Attempted negative deposit");
						System.out.println("Invalid amount");
					}
					else
					{
						//deposits money into current user logged in
						database.deposit(name, deposit);
						//creates log entry of deposit
						database.createTableEntry(name, name, deposit, "Deposit");
						System.out.println("Deposit successful");
					}
					break;
				case 4:
					//transfer case
					System.out.println("Please enter a username to transfer to.");
					scan.nextLine();
					String targetUser = scan.nextLine();
					
					//check if user exists
					if(database.findUser(targetUser) == false)
					{
						LOG.error("User not found");
						System.out.println("User does not exist.");
						break;
					}
					System.out.println("Please enter amount to transfer.");
					int transferAmount = scan.nextInt();
					
					//checks if user has enough funds
					if(database.transferRequest(name, targetUser, transferAmount) == false)
					{
						LOG.error("Insufficient funds");
						System.out.println("Insufficient funds; Transfer not completed");
						break;
					}
					//log entry is done within DAO since DAO reuses deposit and withdraw methods (prevents creation of deposit
					//and withdraw entries whenever a transfer happens)
					System.out.println("Your transfer request has been submitted.");
					break;
				case 5:
					//accept transfer case
					int amount = database.acceptTransfer(name);
					if(amount == -1)
					{
						System.out.println("No pending transfers.");
						break;
					}
					//print accepted funds
					System.out.println("You have accepted a transfer of $" + amount);
					break;
				case 6:
					//logging out case
					temp = false;
					System.out.println("Logging out.");
					scan.close();
					break;
				}
			}
		}
	}
}
