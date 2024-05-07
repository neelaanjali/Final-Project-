package playlists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import program.StatusCode;

// this class will hold the logic for interacting with other users' playlists
// sort of like a social menu
public class PlaylistCatalog {
	/**
	 * Print a list of options a user can take. Have the user make a selection, then execute it
	 * @return StatusCode
	 * @author hargu
	 */
	public static StatusCode printMenu() {
		System.out.println("1 - View all users' playlists");
		System.out.println("2 - Rate a user's playlist");
		System.out.println("3 - View top 5 popular playlists");
		System.out.println("4 - View top 5 users");
		
		PlaylistCatalog playlistCatalog = new PlaylistCatalog();
		return playlistCatalog.executeMenu(playlistCatalog.getMenuSelection());

	}
	
	/**
	 * Execute the user-selection action
	 * @param selection: int
	 * @return StatusCode
	 * @author hargu
	 */
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
	
	/**
	 * Prompt the user to enter a number between 1 and 4, then return it
	 * @return int
	 * @author hargu
	 */
	public int getMenuSelection() {
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
	
	/**
	 * The user may rate the given playlist 1 out of 5. Will then save the changes to file.
	 * @param playlistName
	 * @return StatusCode
	 * @author hargu
	 */
	public StatusCode ratePlaylist(String playlistName) {
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
				ArrayList<Playlist> tempStore = new ArrayList<Playlist>(PlaylistManagerSingleton.playlistList);
				StatusCode result = saveChanges(playlist.getAuthor(), playlists);
				PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>(tempStore);
				return result;
			}
		}
		return StatusCode.NOT_FOUND;
	}
	
	/**
	 * Save the changes made to a playlist
	 * @param authorName
	 * @param allPlaylists
	 * @return StatusCode
	 * @author hargu
	 */
	public StatusCode saveChanges(String authorName, ArrayList<Playlist> allPlaylists) {
		PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
		manager.playlistList.clear();
		
		//add the playlists created by this author
		for(Playlist playlist : allPlaylists) {
			if(playlist.getAuthor().equals(authorName))
				manager.playlistList.add(playlist);
		}
		
		StatusCode opResult = manager.writeToFile(authorName + ".json");
		
		if(opResult != StatusCode.SUCCESS) return opResult;
		return StatusCode.SUCCESS;
	}
	
	/**
	 * View the top 5 highest-rated playlists from all users.
	 * @return StatusCode
	 */
	public StatusCode viewTopPlaylists() {
		ArrayList<File> files = loadPlaylistFiles();
		ArrayList<Playlist> allPlaylists = deserializePlaylists(files);
		
		System.out.println("\nCurrent Top 5 Playlists based on Average Ratings: ");
		
		//set aside what is currently stored in PlaylistManagerSingleton
		PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
		ArrayList<Playlist> tempStor = new ArrayList<Playlist>(PlaylistManagerSingleton.playlistList);
		
		try {
			PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>(allPlaylists);
		} catch (NullPointerException ex) {
			PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>();
		}
		
		PlaylistManagerSingleton.playlistList = sortPlaylistsByRating(PlaylistManagerSingleton.playlistList);
		int count=0;
		for(Playlist playlist : PlaylistManagerSingleton.playlistList)
		{
			if(count>=5) break;
			System.out.println("#" + (count+1) + " Playlist:");
			manager.displayStats(playlist.getPlaylistName());
			count++;
		}

		PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>(tempStor);
		return StatusCode.SUCCESS;
	}
	
	/**
	 * View the top 5 users with the most playlists
	 * @return StatusCode
	 */
	public StatusCode viewTopUsers() {
		//get all playlists
		ArrayList<File> files = loadPlaylistFiles();

		HashMap<String, Integer> userPlaylistCounts = new HashMap<>();
		
		//create new ArrayList of distinct authors
		for (File file : files) {
			String username = getUsernameFromFile(file);
			int count = countPlaylistsInFile(file);
			userPlaylistCounts.put(username, count);			
		}
		
		//sort users by number of playlists (descending):
		List<Map.Entry<String, Integer>> sortedUsers = new ArrayList<>(userPlaylistCounts.entrySet());
		sortedUsers.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
		
		System.out.println("\nCurrent Top 5 Users: ");
		//display top five users
		int index = 0;
		for (Map.Entry<String, Integer> entry : sortedUsers) {
			System.out.println("Top User #" + (index+1) + ": " + entry.getKey() + " - " + entry.getValue() + " playlists");
			index++;
			if (index == 5 || index >= sortedUsers.size()) break;
		}
		
		System.out.println("\n");
		return StatusCode.SUCCESS;
	}
	
	/**
	 * Get a username given a file
	 * @param file
	 * @return String
	 */
	private String getUsernameFromFile(File file) {
		String name = file.getName();
		//remove the '.json' part from the name to return the username
		return name.substring(0, name.lastIndexOf('.'));
	}
	
	/**
	 * Count the number of playlists stored in a file
	 * @param file
	 * @return int: number of playlists stored in the given file
	 */
	private int countPlaylistsInFile(File file) {
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			Pattern pattern = Pattern.compile("\\bplaylistName\\b");
			while ((line=br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					count++;
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * Load all of the username.json files stored
	 * @author hargu
	 * @return ArrayList<File>: all JSON files in the project
	 */
	public ArrayList<File> loadPlaylistFiles() {
		//load .json files into an arraylist called files
		ArrayList<File> files = new ArrayList<File>();
		try {
			File[] filesArray = new File(System.getProperty("user.dir")).listFiles();
			for (File file : filesArray) {
				if(file.getName().contains(".json"))
					files.add(file);
			}
		} catch (Exception e) { e.printStackTrace(); return null; }
		return files;
	}
	
	/**
	 * Deserialize a list of username.json files, and return a list containing all of the deserialized playlists
	 * @author hargu
	 * @param files
	 * @return ArrayList<Playlist>
	 */
	public ArrayList<Playlist> deserializePlaylists(ArrayList<File> files) {
		Gson gson = new Gson();
		ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
		
		try {
			TypeToken<ArrayList<Playlist>> playlistListType = new TypeToken<ArrayList<Playlist>>() {};
			
			for(File file : files) {
				
				if(file.toString().contains("emptyTestCase")) continue;
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				allPlaylists.addAll(gson.fromJson(br, playlistListType));
				br.close();
			}
		}
		catch (Exception e) { return null; }
		return allPlaylists;
	}
	
	/**
	 * View all playlists made by all users
	 * @author hargu
	 * @return StatusCode
	 */
	public StatusCode viewAllPlaylists() {
		ArrayList<File> files = loadPlaylistFiles();
		ArrayList<Playlist> allPlaylists = deserializePlaylists(files);
		
		//set aside what is currently stored in PlaylistManagerSingleton
		PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
		ArrayList<Playlist> tempStor = new ArrayList<Playlist>(PlaylistManagerSingleton.playlistList);
		
		try {
			PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>(allPlaylists);
		} catch (NullPointerException ex) {
			PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>();
		}
				
		for(Playlist playlist : PlaylistManagerSingleton.playlistList)
		{
			manager.displayStats(playlist.getPlaylistName());
		}

		PlaylistManagerSingleton.playlistList = new ArrayList<Playlist>(tempStor);
		return StatusCode.SUCCESS;
	}
	
	/**
	 * Sort all users playlists by rating
	 * @param allPlaylists
	 * @author jxie26
	 * @return ArrayList<Playlist>
	 */
	private ArrayList<Playlist> sortPlaylistsByRating(ArrayList<Playlist> allPlaylists) {
		int n = allPlaylists.size();
		boolean swapped;
		
		do {
			swapped = false;
			for (int i = 1; i < n; i++) {
				if (calculateAvgRating(allPlaylists.get(i-1)) < calculateAvgRating(allPlaylists.get(i))) {
					Playlist temp = allPlaylists.get(i-1);
					allPlaylists.set(i-1, allPlaylists.get(i));
					allPlaylists.set(i, temp);
					swapped = true;
				}
			}
			n--;
		} while(swapped);		
		
		return allPlaylists;
	}
	
	private double calculateAvgRating(Playlist pl) {
		if (pl.getNumOfRatings() == 0) {
			return 0.0;
		}
		else {
			double avg = (double) pl.getSumOfRatings()/pl.getNumOfRatings();
			return avg;
		}
	}
	
}
