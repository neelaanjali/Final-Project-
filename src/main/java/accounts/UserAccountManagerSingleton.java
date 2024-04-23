package accounts;

import java.util.ArrayList;

/**
 * 
 */
public class UserAccountManagerSingleton {
	private ArrayList<String> usernames;
	private ArrayList<String> passwords;
	private ArrayList<UserAccount> accounts;
	private static UserAccountManagerSingleton instance;

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
	public boolean readFromFile() {
		return false;
	}
}
