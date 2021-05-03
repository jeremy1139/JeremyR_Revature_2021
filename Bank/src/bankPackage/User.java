package bankPackage;

import java.util.Scanner;

public class User 
{
	private String username;
	private String password;
	
	private User()
	{
		//user must have name/pass
	}
	
	public User(String name, String pass)
	{
		username = name;
		pass = password;
	}
	//Main function that displays options for user
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
						break;
					case 5:
						break;
					case 6:
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
		System.out.println("(1) View Balance");
		System.out.println("(2) Withdraw");
		System.out.println("(3) Deposit");
		System.out.println("(4) Transfer money");
		System.out.println("(5) Accept Transfer");
		System.out.println("(6) Log out");
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
