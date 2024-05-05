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
		//this will loop until the user successfully logs in or registers
		while(true)
		{
			StatusCode result = accountManager.welcome();
			if(result == StatusCode.SUCCESS)
			{
				// user is successfully logged in
				System.out.println("You have succesfully logged in!");
				break;
			}
			else if(result == StatusCode.NOT_FOUND)
			{
				System.out.println("The information you entered did not match our records.");
			}
			else if(result == StatusCode.INVALID_INPUT)
			{
				System.out.println("The information you entered is invalid.");
			}
			else
			{
				// user did not get logged in
				System.out.println("Sorry, something went wrong.");
				System.exit(1);
			}
		}
		System.out.println();
		
		
		//ask the user what they would like to do now
		PlaylistManagerSingleton playlistManager = PlaylistManagerSingleton.getInstance();
		
		//load in the users playlists
		StatusCode result = playlistManager.readFromFile(username + ".json");
		if(result == StatusCode.NOT_FOUND)
		{
			//create a new file for the user
			playlistManager.writeToFile(username + ".json");
			playlistManager.readFromFile(username + ".json");
		}
		else if (result == StatusCode.EXCEPTION)
		{
			System.out.println("Sorry, an error occurred while loading your playlists.");
			System.exit(1);
		}
		
		//this infinite loop will continue until the user chooses to exit
		while(true)
		{
			playlistManager.printMainMenu();
			int selection = playlistManager.getMainMenuSelection();
			result = playlistManager.executeMainMenu(selection);
			
			switch(result) {
			case SUCCESS:
				continue;
			case EXCEPTION:
			case FAILURE:
				System.out.println("Sorry, something went wrong");
				System.exit(1);
				break;
			case NOT_FOUND:
				System.out.println("Sorry, your input was not found.");
				continue;
			case INVALID_INPUT:
				System.out.println("Sorry, your input was invalid.");
				continue;
			case NOT_IMPLEMENTED:
				System.out.println("Sorry, this feature has not yet been implemented.");
				continue;
			case EXIT:
				System.exit(0);
				break;
			}
		}
	}

}
