package program;

import accounts.UserAccountManagerSingleton;
import playlists.PlaylistManagerSingleton;

public class Main {
	
	//this static global variable will hold the username of the user after they have successfully logged in
	//	if nobody is logged in, username will be null
	public static String username = null;

	public static void main(String[] args) {
		//initialize the UserAccountManagerSingleton
		UserAccountManagerSingleton accountManager = UserAccountManagerSingleton.getInstance();
		
		//welcome the user
		//this method will ask the user to login/register
		if(accountManager.welcome())
		{
			// user is successfully logged in
			System.out.println("You have succesfully logged in!");
		}
		else
		{
			// user did not get logged in
			System.out.println("Sorry, something went wrong.");
			System.exit(0);
		}
		
		//ask the user what they would like to do now
		PlaylistManagerSingleton playlistManager = PlaylistManagerSingleton.getInstance();
		//this infinite loop will continue until the user chooses to exit
		while(true)
		{
			playlistManager.choiceMenu();
		}
	}

}
