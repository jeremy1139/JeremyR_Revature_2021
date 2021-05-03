package bankPackage;

import java.util.Scanner;

public class BankSystem 
{
	//System functions currently handled in main, might have to change later
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Would you like to (1)register or (2)login?");
		int num = scan.nextInt();
		if(num == 1)
		{
			System.out.println("Please enter a username");
			String name = scan.nextLine();
			//todo check if username exists in database
			
			System.out.println("Please enter a password");
			String pass = scan.nextLine();
			
			//todo create new user entry in database
			
			System.out.println("Account created");
		}
		if(num == 2)
		{
			System.out.println("Please enter a username");
			String name = scan.nextLine();
			//todo check if username exists in database
			
			System.out.println("Please enter a password");
			String pass = scan.nextLine();
			//todo check if user/password combo matches
			
			//Password checking is done in user currently, might have to change later
		}
	}

}
