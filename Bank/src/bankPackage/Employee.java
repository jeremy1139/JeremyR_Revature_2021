package bankPackage;

import java.util.Scanner;

public class Employee 
{
	private String username;
	private String password;
	
	private Employee()
	{
		//Employee must have a name/pass
	}
	
	public Employee(String name, String pass)
	{
		username = name;
		pass = password;
	}
	
	//Main function that presents options for employees when name/pass matches what is stored
	public void login(String name, String pass)
	{
		if(name.equals(username))
		{
			if(pass.equals(password))
			{
				boolean temp = true;
				while(temp)
				{
					printOptions();
					//get user input
					Scanner scan = new Scanner(System.in);
					int num = scan.nextInt();
					switch(num)
					{
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						temp = false;
						scan.close();
						break;
					}
				}
			}
		}
		else
		{
			System.out.println("Invalid username/password");
		}
	}
	
	private void printOptions()
	{
		System.out.println("Please select a option");
		System.out.println("(1) Approve/Reject account");
		System.out.println("(2) View customer bank account");
		System.out.println("(3) View log of all transactions");
		System.out.println("(4) Log out");
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
