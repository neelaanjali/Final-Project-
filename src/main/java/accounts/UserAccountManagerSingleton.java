package accounts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.io.Files;

import program.Main;
import program.StatusCode;

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
		readFromFile(userAccountsFile);
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
	public StatusCode welcome() {
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
			System.out.println("DEBUG");
			try 
			{
				userSelection = scanner.nextInt();
				System.out.println("DEBUG: selection = " + userSelection);
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
			String[] loginInfo = getLoginInfo(scanner);
			return login(loginInfo[0], loginInfo[1]);
		case 2:
			String[] registrationInfo = getLoginInfo(scanner);
			return this.register(registrationInfo[0], registrationInfo[1]);
		default:
			return StatusCode.EXIT;
		}
	}
	
	public String[] getLoginInfo(Scanner scanner)
	{
		String[] loginInfo = new String[2];
		
		System.out.print("Please enter your username: ");
		loginInfo[0] = scanner.nextLine();
		System.out.print("\nPlease enter your password: ");
		loginInfo[1] = hashPassword(scanner.nextLine());
		
		return loginInfo;
	}
	
	/**
	 * Login to a user account
	 * @author hargu
	 * @return StatusCode.SUCCESS or StatusCode.NOT_FOUND or StatusCode.INVALID_INPUT
	 */
	public StatusCode login(String username, String password) {
		if(username == null || password == null)
		{
			return StatusCode.INVALID_INPUT;
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
			Main.username = username;
			return StatusCode.SUCCESS;
		}
		//the username and password did not match
		else
		{
			return StatusCode.NOT_FOUND;
		}
	}
	
	/**
	 * Register a new user account
	 * @author hargu
	 * @return StatusCode.INVALID_INPUT or StatusCode.SUCCESS
	 */
	public StatusCode register(String username, String password) {	
		if (username == null || password == null)
			return StatusCode.INVALID_INPUT;
		
		//check that the username has not already been taken
		if(usernames.contains(username))
		{
			return StatusCode.INVALID_INPUT;
		}
		
		//the password does not meet the 8 character minimum
		if(password.length() < 8)
		{
			return StatusCode.INVALID_INPUT;
		}
		
		System.out.println("You have succesfully created a new account.");
		
		//store the username and password
		usernames.add(username);
		passwords.add(password);
		
		//update the file
		this.writeToFile(userAccountsFile);
		
		Main.username = username;
		return StatusCode.SUCCESS;
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
	 * @author jxie26
	 * @return boolean indicating success or failure
	 */
	public StatusCode writeToFile(String filePath) {
		try {
			FileWriter fw = new FileWriter(filePath);
			BufferedWriter bw = new BufferedWriter(fw);
			
			//clear contents
			bw.write("");
			
			//write header line
			bw.write("Usernames,Passwords\n");
			
			//loop through ArrayLists and write each username & password pair to file
			for (int i = 0; i < usernames.size(); i++) {
                String line = usernames.get(i) + "," + passwords.get(i) + "\n";
                bw.write(line);
			}
			bw.close();
			return StatusCode.SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			return StatusCode.EXCEPTION;
		}
	}
	
	/**
	 * Read stored usernames and passwords into their respective ArrayLists
	 * @author jxie26
	 * @return boolean
	 */
	public StatusCode readFromFile(String filePath) {
		try {
			//open the file
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine(); 	//skip the first line (headers) in the file
			
			String[] line;
			String unsplitLine;
			
			//loop through each line in the file (each line is an account)
			unsplitLine = br.readLine();
			while(unsplitLine != null) {
				line = unsplitLine.split(",");
				
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
			return StatusCode.SUCCESS;
		}
		
		catch (FileNotFoundException e) {
			System.out.println("The file could not be opened.");
			e.printStackTrace();
			return StatusCode.NOT_FOUND;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return StatusCode.EXCEPTION;
		}
	}
	
	public static String getUseraccountsfile() {
		return userAccountsFile;
	}
	
	
}
