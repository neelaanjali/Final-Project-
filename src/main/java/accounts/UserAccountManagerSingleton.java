package accounts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
	}
	
	public static UserAccountManagerSingleton getInstance() {
		//create the instance if it doesn't exist yet
		if(instance == null) {
			instance = new UserAccountManagerSingleton();
		}
		return instance;
	}

	
	/**
	 * Login to a user account
	 * @author hargu
	 * @return 
	 */
	public boolean login() {
		return false;
	}
	
	/**
	 * Register a new user account
	 * @author hargu
	 * @return
	 */
	public boolean register() {
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
