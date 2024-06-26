package playlists;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import program.Main;
import program.StatusCode;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlaylistManagerSingleton {
	
	/**
	 * @author riannaellis
	 */
	private static PlaylistManagerSingleton instance;
	public static ArrayList<Playlist> playlistList;
	
	/**
	 * Default constructor
	 * @author riannaellis
	 */
	public PlaylistManagerSingleton() {
		playlistList = new ArrayList<Playlist>();
	}
	  
	/**
	 * Get the static instance of the PlaylistManagerSingleton class
	 * @author riannaellis
	 * @return The static instance of the PlaylistManagerSingleton class
	 */
	public static PlaylistManagerSingleton getInstance() {
		//create the instance if it doesn't exist yet
        if (instance == null) {
            instance = new PlaylistManagerSingleton();
        }
        return instance;
	}
	
	/**
	 * Reads the user's playlists from a file
	 * @author riannaellis
	 * @return EXCEPTION or SUCCESS or NOT_FOUND
	 * @param filePath
	 */
    public StatusCode readFromFile(String filePath) {
    	Gson gson = new Gson();
    	
    	try {
    		//Create a new reader
    		BufferedReader br = new BufferedReader(new FileReader(filePath));
    		
    		TypeToken<ArrayList<Playlist>> playlistListType = new TypeToken<ArrayList<Playlist>>() {};

    		//Read from the file and add the playlists to playlistList
    		playlistList = gson.fromJson(br, playlistListType);
    		
    		//Ensure the file wasn't empty
    		if (playlistList == null) {
    			throw new Exception();
    		}
    		
    		return StatusCode.SUCCESS;

          }
    	catch (FileNotFoundException e) {
    		return StatusCode.NOT_FOUND;
        }
    	catch (Exception e){
    		return StatusCode.EXCEPTION;
    	}
    }
    
	/**
	 * Writes all  of the user's playlists to a file
	 * @author riannaellis
	 * @return EXCEPTION or SUCCESS
	 * @param filePath
	 */
    public StatusCode writeToFile(String filePath) {
    	Gson gson = new Gson();
    	//Converts the list of playlists to json
    	String json = gson.toJson(playlistList);
    	
    	try {
    		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    		//Writes the playlistList to a json file
    		bw.write(json);
    		bw.close();
            return StatusCode.SUCCESS;
            
    	} catch (IOException e) {
            return StatusCode.EXCEPTION;
        }
    }
    
    /**
     * Will prompt the user to enter the name of a playlist. Return null on exception.
     * @author hargu
     * @return String: name the user entered
     */
    public String askPlaylistName() {
    	System.out.println("Enter the playlist name:");
    	Scanner scanner = new Scanner(System.in);
    	String playlistName;
    	try
    	{
    		playlistName = scanner.nextLine().trim();
    	}
    	catch (Exception e)
    	{
    		return null;
    	}
    	return playlistName;
    }
    
	/**
	 * Uses the name of a playlist to display stats about that playlist
	 * @author riannaellis
	 * @return SUCCESS or NOT_FOUND or INVALID_INPUT
	 */
    public StatusCode displayStats(String playlistName) {  	
    	if (playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
    	for (Playlist playlist : playlistList) {
    		if (playlist.getPlaylistName().equals(playlistName)) {
    			
    			System.out.println("---------------------------");
    			
    			System.out.println("Playlist name: " + playlist.getPlaylistName());
    			System.out.println("Author: " + playlist.getAuthor());
    			
    			// format length
    			int totalSeconds = 0;
    			for (Song song : playlist.getSongs()) {
    				totalSeconds += song.getLength();
    			}
    			
    			Integer totalMinutes = totalSeconds / 60;
    			Integer seconds = totalSeconds % 60;
    			
    			//if seconds is less than 10 seconds, append a 0 to the front
    			String sec = "";
    			if(seconds < 10) {
    				sec = "0" + seconds.toString();
    			}
    			else {
    				sec = seconds.toString();
    			}
    			
    			String totalTime = totalMinutes.toString() + ":" + sec;
    			
    			System.out.println("Total Length: " + totalTime);
    			System.out.println("Rating: " + String.format("%.2f", ((double)playlist.getSumOfRatings())/playlist.getNumOfRatings()) + "/5");
    			System.out.println("Number of Songs: " + playlist.getSongs().size());
    			
    			for (Song song : playlist.getSongs()) {
    				System.out.println(" * " + song.toString());
    			}
    			System.out.println("---------------------------");
    			System.out.println();

    			return StatusCode.SUCCESS;
    		}
    	}
   		return StatusCode.NOT_FOUND;
    }
    
    /**
     * Add a new playlist to the user's account
     * @param playlistName: name of a new playlist
     * @return INVALID_INPUT or SUCCESS
     */
    public StatusCode addNewPlaylist(String playlistName) {
    	if(playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
         ArrayList<Song> songs = new ArrayList<Song>();
         
         Playlist newPlaylist = new Playlist(Main.username, playlistName, songs);
         
         for(Playlist playlist : playlistList)
         {
        	 if(playlist.getPlaylistName() == newPlaylist.getPlaylistName())
        	 {
                 System.out.println("Sorry that playlist already exists!\n");
                 return StatusCode.INVALID_INPUT;
        	 }
         }
  
         playlistList.add(newPlaylist);
         System.out.println("Playlist created successfully!\n");
         return StatusCode.SUCCESS;
    }
    
    
    public StatusCode deletePlaylist(String playlistName) {
       if(playlistName == null)
       return StatusCode.INVALID_INPUT;    	    

       for (Playlist playlist : playlistList) {
           if (playlist.getPlaylistName().equals(playlistName)) {
               playlistList.remove(playlist);
               // Optionally, you can print a message indicating successful deletion
               System.out.println("Playlist '" + playlistName + "' deleted successfully.");
               // Call writeToFile to save the changes to the file
               writeToFile(Main.username + ".json");
               return StatusCode.SUCCESS;
           }
       }

       // Playlist not found
       System.out.println("Sorry Playlist '" + playlistName + "' not found.");
       return StatusCode.NOT_FOUND;
    }
    
    /**
     * Calls Playlist.editPlaylist() on the user-selected Playlist object
     * @param playlistName
     * @return StatusCode
     */
    public StatusCode editPlaylist(String playlistName) {
    	if(playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
    	for(Playlist playlist : playlistList) {
    		if(playlist.getPlaylistName().equals(playlistName)) {
    			StatusCode result = playlist.editPlaylist();
    			writeToFile(Main.username + ".json");
    			return result;
    		}
    	}	
        return StatusCode.NOT_FOUND;
    }
        
    /**
     * Search all of a user's playlist for a song given its name
     * @param songName
     * @return ArrayList<Song>: all songs that match this name
     */
    public ArrayList<Song> searchSongsBySongName(String songName) {
        ArrayList<Song> locatedSongs = new ArrayList<>();
        for (Playlist playlist : playlistList) {
            for (Song song : playlist.getSongs()) {
                if (song.getSongName().equalsIgnoreCase(songName)) {
                    locatedSongs.add(song);
                }
            }
        }
        if (!locatedSongs.isEmpty()) {
            System.out.println("Search for song '" + songName + "' was successful.");
        } else {
            System.out.println("No songs found matching the name '" + songName + "'.");
        }
      
        return locatedSongs;
    }
    
    /**
     * View all of a user's playlists
     * @return StatusCode
     */
    public StatusCode viewPlaylists()
    {
    	System.out.println("\nHere are your playlists:");
    	if(playlistList.size() < 1)
    	{
    		System.out.println("\nYou have no playlists");
    		return StatusCode.SUCCESS;
    	}
    	for(Playlist playlist : this.playlistList)
    	{
    		System.out.println(" * " + playlist.getPlaylistName());
    	}
    	System.out.println("\n");
    	return StatusCode.SUCCESS;
    }
    
    /**
     * Ask the user how they would like to search a song, then call the corresponding method
     * @return StatusCode
     */
    private StatusCode searchSongs() {
    	System.out.println("Search by...");
    	System.out.println("1 - Song Name");
    	System.out.println("2 - Artist Name");
    	System.out.println("3 - Song Length");
    	
    	int userSelection = -1;
		Scanner scanner = new Scanner(System.in);
		try 
		{
			userSelection = scanner.nextInt();
			if (userSelection < 1 || userSelection > 3)
			{
				System.out.println("Enter a number between 1 and 3: ");
				return searchSongs();
			}
		} 
		catch (Exception e)
		{
			return StatusCode.EXCEPTION;
		}
		
		switch(userSelection) {
		case 1:
			String songName = searchByName();
			ArrayList<Song> songNames = searchSongsBySongName(songName);
			
			for(Song song : songNames) {
				System.out.println(song.toString());
			}
			
			if (songNames.isEmpty()) return StatusCode.NOT_FOUND;
			else return StatusCode.SUCCESS;
			
		case 2:
			ArrayList<Song> songs = searchByArtist();
			for (Song song : songs) {
				System.out.println(song.toString());
			}
			if (songs.isEmpty()) return StatusCode.NOT_FOUND;
			else return StatusCode.SUCCESS;
		case 3:
			return searchByLength();
		default:
			return StatusCode.INVALID_INPUT;
		}
    }
    
    /**
     * Ask the user the name of the song they would like to search
     * @return String
     */
    private String searchByName() {
    	System.out.println("Enter the name of the song you would like to search:");
    	Scanner scanner = new Scanner(System.in);
    	String songName;
    	
    	try {
    		songName = scanner.nextLine();
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	
    	return songName;
    }
    
    /**
     * Asks the user for an artist name, then searches all their playlists for songs by that artist
     * @return ArrayList<Song>: all songs that match the artist name
     */
    private ArrayList<Song> searchByArtist() {
    	System.out.println("Enter the full name of the artist to see their songs: ");
    	Scanner scanner = new Scanner(System.in);
    	String artist;
		ArrayList<Song> foundSongs = new ArrayList<>();
    	try {
    		artist = scanner.nextLine();
            for (Playlist playlist : playlistList) {
                for (Song song : playlist.getSongs()) {
                    if (song.getArtistName().equalsIgnoreCase(artist)) {
                        foundSongs.add(song);
                    }
                }
            }
            if (!foundSongs.isEmpty()) {
                System.out.println(foundSongs.size() + " song(s) found in your playlists by '" + artist + "':");   
            } 
            else {
                System.out.println("No songs were found by the artist '" + artist + "'.");
            }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return foundSongs;
    }
    
    /**
     * Search all of a user's playlists for a song given a specified length
     * @return StatusCode
     */
    private StatusCode searchByLength() {
    	System.out.println("Enter the length (MM:SS) of the song you'd like to search for: ");
    	Scanner scanner = new Scanner(System.in);
    	String length = scanner.nextLine();
    	
    	try {
    		String[] time = length.split(":");
            int seconds = (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
            
    		ArrayList<Song> foundSongs = new ArrayList<>();
            for (Playlist playlist : playlistList) {
                for (Song song : playlist.getSongs()) {
                    if (song.getLength() == seconds) {
                        foundSongs.add(song);
                    }
                }
            }
            if (!foundSongs.isEmpty()) {
                System.out.println(foundSongs.size() + " song(s) found in your playlist of length " + length + ".");
                for (Song song : foundSongs) {
                	System.out.println(song.toString());
                }
            } else {
                System.out.println("No songs of length " + length + " were found.");
                return StatusCode.NOT_FOUND;
            }
            return StatusCode.SUCCESS;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return StatusCode.EXCEPTION;
    	}
    }
    
    /**
     * The main operating logic of the program. Will continually ask the user what they would like to do.
     * @return SUCCESS or FAILURE or EXCEPTION or NOT_FOUND
     * @author hargu
     */
    public StatusCode executeMainMenu(int userSelection) {	
		
		StatusCode opStatus;
		StatusCode saveStatus;
		
		switch (userSelection) {
		case 1:
			opStatus = addNewPlaylist(askPlaylistName());
			if (opStatus != StatusCode.SUCCESS)
				return opStatus;
			
			saveStatus = writeToFile(Main.username + ".json");
			if (saveStatus != StatusCode.SUCCESS)
				return saveStatus;
			
			return StatusCode.SUCCESS;
			
		case 2:
			opStatus = deletePlaylist(askPlaylistName());
			if (opStatus != StatusCode.SUCCESS)
				return opStatus;
			
			saveStatus = writeToFile(Main.username + ".json");
			if (saveStatus != StatusCode.SUCCESS)
				return saveStatus;
			
			return StatusCode.SUCCESS;
			
		case 3:
			opStatus = editPlaylist(askPlaylistName());
			if (opStatus != StatusCode.SUCCESS)
				return opStatus;
			
			saveStatus = writeToFile(Main.username + ".json");
			if (saveStatus != StatusCode.SUCCESS)
				return saveStatus;
			
			return StatusCode.SUCCESS;
			
		case 4:
			return displayStats(askPlaylistName());
		case 5:
			return viewPlaylists();
		case 6:
			return searchSongs();
		case 7:
			return PlaylistCatalog.printMenu();
		case 8:
			return StatusCode.EXIT;
		default:
			return StatusCode.INVALID_INPUT;
		}
    }
    
	/**
	 * Displays the main menu
	 * @author riannaellis
	 * @return SUCCESS
	 */
    public StatusCode printMainMenu() {
    	System.out.println("1 - Add a new playlist");
    	System.out.println("2 - Delete a playlist");
    	System.out.println("3 - Edit a playlist");
    	System.out.println("4 - View a playlist");
    	System.out.println("5 - View all my playlists");
    	System.out.println("6 - Search my songs");
    	System.out.println("7 - View social menu");
    	System.out.println("8 - Exit");
    	System.out.print("What would you like to do? ");
    	return StatusCode.SUCCESS;
    }
    
    /**
     * Ask the user to enter a number between 1 and 8, then return it
     * @return an integer between 1-8 indicating the users selection. Or -1 upon failure.
     * @author hargu
     */
    public int getMainMenuSelection() {
    	int userSelection = -1;
		Scanner scanner = new Scanner(System.in);
		try 
		{
			userSelection = scanner.nextInt();
			if (userSelection < 1 || userSelection > 8)
			{
				System.out.println("Enter a number between 1 and 8: ");
				return getMainMenuSelection();
			}
		} 
		catch (Exception e)
		{
			return -1;
		}
		return userSelection;
    }
}