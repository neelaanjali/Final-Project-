package accounts;

import java.util.ArrayList;

/**
 * 
 */
public class UserAccountManagerSingleton {
	private ArrayList<String> usernames;
	private ArrayList<String> passwords;
	private ArrayList<UserAccount> accounts;
	
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
	 * @return
	 */
	private boolean readFromFile() {
		return false;
	}
	
}
