package playlists;

import java.util.Scanner;

import program.StatusCode;

// this class will hold the logic for viewing other users' playlists
// sort of like a social menu
public class PlaylistCatalog {
	public static StatusCode printMenu() {
		System.out.println("1 - View all users' playlists");
		System.out.println("2 - Rate a user's playlist");
		System.out.println("3 - View top 5 popular playlists");
		System.out.println("4 - View top 5 users");
		
		PlaylistCatalog playlistCatalog = new PlaylistCatalog();
		playlistCatalog.executeMenu(playlistCatalog.getMenuSelection());

	}
	
	private StatusCode executeMenu(int selection) {
		switch(selection) {
		case 1:
			return viewAllPlaylists();
		case 2:
			return ratePlaylist();
		case 3:
			return viewTopPlaylists();
		case 4:
			return viewTopUsers();
		default:
			return StatusCode.INVALID_INPUT;
		}
	}
	
	private int getMenuSelection() {
		int userSelection = -1;
		Scanner scanner = new Scanner(System.in);
		try 
		{
			userSelection = scanner.nextInt();
			if (userSelection < 1 || userSelection > 4)
			{
				System.out.println("Enter a number between 1 and 4: ");
				return getMenuSelection();
			}
		} 
		catch (Exception e)
		{
			return -1;
		}
		return userSelection;
	}
	
	private StatusCode viewAllPlaylists() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode ratePlaylist() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode viewTopPlaylists() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode viewTopUsers() {
		return StatusCode.NOT_IMPLEMENTED;
	}
}
