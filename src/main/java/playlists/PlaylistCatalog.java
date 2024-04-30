package playlists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		return playlistCatalog.executeMenu(playlistCatalog.getMenuSelection());

	}
	
	public StatusCode executeMenu(int selection) {
		switch(selection) {
		case 1:
			return viewAllPlaylists();
		case 2:
			return ratePlaylist(PlaylistManagerSingleton.getInstance().askPlaylistName());
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
	
	private StatusCode ratePlaylist(String playlistName) {
		if(playlistName == null) { return StatusCode.INVALID_INPUT; }
		
		ArrayList<Playlist> playlists = deserializePlaylists(loadPlaylistFiles());
		
		for(Playlist playlist : playlists)
		{
			if(playlist.getPlaylistName().equals(playlistName))
			{
				int userRating;
				
				try {
					Scanner scanner = new Scanner(System.in);
					System.out.println("Rate playlist '" + playlistName + "' 1 out of 5: ");
					userRating = scanner.nextInt();
				} catch (Exception e) { return StatusCode.EXCEPTION; }
				
				if (userRating < 1 || userRating > 5)
				{
					System.out.println("Please enter an integer between 1 and 5");
					return ratePlaylist(playlistName);
				}
				
				playlist.setNumOfRatings(playlist.getNumOfRatings() + 1);
				playlist.setSumOfRatings(playlist.getSumOfRatings() + userRating);
				
				// save changes
				return saveChanges(playlist.getAuthor(), playlists);
			}
		}
		return StatusCode.NOT_FOUND;
	}
	
	public StatusCode saveChanges(String authorName, ArrayList<Playlist> allPlaylists) {
		PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
		//set aside the contents in PlaylistManager
		ArrayList<Playlist> tempStor = manager.playlistList;
		manager.playlistList.clear();
		
		//add the playlists created by this author
		for(Playlist playlist : allPlaylists) {
			if(playlist.getAuthor().equals(authorName))
				manager.playlistList.add(playlist);
		}
		
		StatusCode opResult = manager.writeToFile(authorName + ".json");
		manager.playlistList = tempStor;
		
		if(opResult != StatusCode.SUCCESS) return opResult;
		return StatusCode.SUCCESS;
	}
	
	private StatusCode viewTopPlaylists() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode viewTopUsers() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	public ArrayList<File> loadPlaylistFiles() {
		//load .json files into an arraylist called files
		ArrayList<File> files = new ArrayList<File>();
		try {
			File[] filesArray = new File(System.getProperty("user.dir")).listFiles();
			for (File file : filesArray) {
				if(file.getName().contains(".json"))
					files.add(file);
			}
		} catch (Exception e) { return null; }
		return files;
	}
	
	public ArrayList<Playlist> deserializePlaylists(ArrayList<File> files) {
		Gson gson = new Gson();
		ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
		
		try {
			TypeToken<ArrayList<Playlist>> playlistListType = new TypeToken<ArrayList<Playlist>>() {};
			
			for(File file : files) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				allPlaylists.addAll(gson.fromJson(br, playlistListType));
				br.close();
			}
		}
		catch (Exception e) { return null; }
		return allPlaylists;
	}
	
	public StatusCode viewAllPlaylists() {
		ArrayList<File> files = loadPlaylistFiles();
		ArrayList<Playlist> allPlaylists = deserializePlaylists(files);
		
		//set aside what is currently stored in PlaylistManagerSingleton
		PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
		ArrayList<Playlist> tempStor = manager.playlistList;
		
		manager.playlistList = allPlaylists;
		
		for(Playlist playlist : manager.playlistList)
		{
			manager.displayStats(playlist.getPlaylistName());
		}

		manager.playlistList = tempStor;
		return StatusCode.SUCCESS;
	}
	
}
