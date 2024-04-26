package accounts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import program.Main;

/**
 * 
 */
public class UserAccountManagerSingleton {
	private ArrayList<String> usernames;
	private ArrayList<String> passwords;
	private ArrayList<UserAccount> accounts;
	private static UserAccountManagerSingleton instance;
	private final static String userAccountsFile = "userAccounts.csv";

	//constructor:
	private UserAccountManagerSingleton() { 
		this.usernames = new ArrayList<String>();
		this.passwords = new ArrayList<String>();
		this.accounts = new ArrayList<UserAccount>();
		readFromFile();
	}
	
	public static UserAccountManagerSingleton getInstance() {
		//create the instance if it doesn't exist yet
		if(instance == null) {
			instance = new UserAccountManagerSingleton();
		}
		return instance;
	}

	/**
	 * Welcome a user to the Playlist manager. Ask the user what they would like to do
	 */
	public boolean welcome() {
		//ask the user what they would like to do
		System.out.println("Welcome to the Playlist manager!");
		System.out.println("1 - Login");
		System.out.println("2 - Register");
		System.out.println("3 - Exit");
		System.out.println("Please select an option: ");
		
		//process the user's selection
		int userSelection = 0;
		Scanner scanner = new Scanner(System.in);
		while(true) 
		{
			try 
			{
				userSelection = scanner.nextInt();
				if (userSelection < 1 || userSelection > 3)
					throw new Exception();
				break;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Please enter 1, 2, or 3");
				continue;
			}
		}
		
		switch (userSelection) {
		case 1:
			return this.login();
		case 2:
			return this.register();
		case 3:
			System.exit(0);
			return true;
		}
		return false;
	}
	
	/**
	 * Login to a user account
	 * @author hargu
	 * @return 
	 */
	private boolean login() {
		Scanner scanner = new Scanner(System.in);
		String username = "", password = "";
		
		// this infinite while loop will run until the user enters the correct information,
		//    or if they choose to register a new account, or exit the program
		while(true)
		{
			// get user's username and password from input
			try
			{
				System.out.print("Please enter your username: ");
				username = scanner.nextLine();
				System.out.print("\nPlease enter your password: ");
				password = hashPassword(scanner.nextLine());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
			
			//check if there exists a matching username/password pair
			boolean isFound = false;
			for(int i = 0; i < usernames.size(); i++) 
			{
				if(usernames.get(i).equals(username) && passwords.get(i).equals(password)) 
				{
					//the username and password matches
					isFound = true;
				}
			}
			
			//take the correct action
			if(isFound) 
			{
				System.out.print("Welcome " + username + ". ");
				//take whatever action is necessary
				Main.username = username;
				return true;
			}
			//the username and password did not match
			else if(!isFound)
			{
				//display the user's options
				System.out.println("Sorry, the username and password you entered do not match.");
				System.out.println("1 - Try Again");
				System.out.println("2 - Register New Account");
				System.out.println("3 - Exit");
				System.out.print("Please make your selection: ");
				
				// get the users selection
				int userSelection = 0;
				// this infinite loop will run until the user inputs a valid selection (1, 2, or 3)
				while(true)
				{
					try 
					{
						userSelection = scanner.nextInt();
						if (userSelection < 1 || userSelection > 3) {
							throw new Exception();
						}
						break;
					} 
					catch (Exception e) 
					{
						//System.out.println("Please enter 1, 2, or 3: ");
						continue;
					}
				}
				
				// take the correct action, based on the users selection
				switch(userSelection) {
				case 1:
					scanner.nextLine(); //flush
					continue;
				case 2:
					return this.register();
				case 3:
					System.exit(0);
					return true;
				}
			}
		}
	}
	
	/**
	 * Register a new user account
	 * @author hargu
	 * @return
	 */
	private boolean register() {
		Scanner scanner = new Scanner(System.in);
		String username = "", password = "";
		
		//ask the user what username they would like
		System.out.print("Please enter a username: ");
		
		//this infinite while loop will continue until the user inputs a valid username, or chooses to login instead
		while(true) 	
		{
			username = scanner.nextLine();
			
			//the user has decided to try to login instead (prompted after 1 failed attempt)
			if(username == "-1") 
			{
				return this.login();
			}
			
			//check that the username has not already been taken
			if(usernames.contains(username))
			{
				//ask the user for another username
				System.out.println("This username is already taken. Please enter a different username, or type '-1' to login.");
				continue;
			}
			//the username has not been taken
			else break;
		}
		
		//ask the user for a password
		System.out.println("Please enter a password. Passwords must be 8 characters or greater:");
		
		//this infinite while loop will continue until the user inputs a valid password
		while(true)
		{
			password = scanner.nextLine();
			
			//the password does not meet the 8 character minimum
			if(password.length() < 8)
			{
				System.out.println("Please enter a password that is at least 8 characters:");
				continue;
			}
			else 
			{	
				System.out.println("You have succesfully created a new account.");
				break;
			}
		}
		
		//encrypt the password
		password = hashPassword(password);
		
		//store the username and password
		usernames.add(username);
		passwords.add(password);
		
		//update the file
		this.writeToFile();
		
		Main.username = username;
		return true;
	}
	
	/**
	 * Hash a password
	 * @param unhashed: the string password that should be hashed
	 * @return the hashed password
	 */
	private String hashPassword(String unhashed) {
		return unhashed;
	}
	
	/**
	 * Store the changes made to user accounts
	 * @return boolean indicating success or failure
	 */
	private boolean writeToFile() {
		return false;
	}
	
	/**
	 * Read stored usernames and passwords into their respective ArrayLists
	 * @author jxie26
	 * @return boolean
	 */
	private boolean readFromFile() {
		try {
			//open the file
			FileReader fr = new FileReader(userAccountsFile);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine(); 	//skip the first line (headers) in the file
			
			String[] line;
			String unsplitLine;
			
			//loop through each line in the file (each line is an account)
			unsplitLine = br.readLine();
			while(unsplitLine != null) {
				line = unsplitLine.split(",");
				
				//check that line was correctly split into 2 parts (username & pw)
				if(line.length != 2) {
					break;
				}
				
				//read each line into usernames and passwords ArrayLists
				String username = line[0].trim();
				String password = line[1].trim();
				
				this.usernames.add(username);
				this.passwords.add(password);
				
				unsplitLine = br.readLine();
			}
			
			//close file and buffered readers
			br.close();
			fr.close();
			return true;
		}
		
		catch (FileNotFoundException e) {
			System.out.println("The file could not be opened.");
			e.printStackTrace();
			return false;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	  
	
	
}
