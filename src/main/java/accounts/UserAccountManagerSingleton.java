package accounts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		System.out.print("Please select an option: ");
		
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
				System.out.println("Please enter 1, 2, or 3");
				continue;
			}
		}
		scanner.close();
		
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
				scanner.close();
			}
			
			//check if their exists a matching username/password pair
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
				System.out.println("Welcome " + username + ". You have successfully logged in.");
				//take whatever action is necessary
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
						System.out.println("Please enter 1, 2, or 3: ");
						continue;
					}
				}
				
				// take the correct action, based on the users selection
				switch(userSelection) {
				case 1:
					continue;
				case 2:
					scanner.close();
					return this.register();
				case 3:
					scanner.close();
					System.exit(0);
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
		return false;
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
