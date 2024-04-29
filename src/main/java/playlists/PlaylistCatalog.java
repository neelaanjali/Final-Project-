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
	
	private StatusCode ratePlaylist() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode viewTopPlaylists() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode viewTopUsers() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private ArrayList<File> loadPlaylistFiles() {
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
	
	private ArrayList<Playlist> deserializePlaylists(ArrayList<File> files) {
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
	
	private StatusCode viewAllPlaylists() {
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
