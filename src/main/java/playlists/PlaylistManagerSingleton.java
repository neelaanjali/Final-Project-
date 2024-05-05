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
	
	private static PlaylistManagerSingleton instance;
	public static ArrayList<Playlist> playlistList;
	
	public PlaylistManagerSingleton() {
		playlistList = new ArrayList<Playlist>();
	}
	  
	
	public static PlaylistManagerSingleton getInstance() {
		//create the instance if it doesn't exist yet
        if (instance == null) {
            instance = new PlaylistManagerSingleton();
        }
        return instance;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return SUCCESS or NOT_FOUND or EXCEPTION
	 */
    public StatusCode readFromFile(String filePath) {
    	Gson gson = new Gson();
    	
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(filePath));
    		
    		TypeToken<ArrayList<Playlist>> playlistListType = new TypeToken<ArrayList<Playlist>>() {};

    		playlistList = gson.fromJson(br, playlistListType);
    		
    		return StatusCode.SUCCESS;

          }
    	catch (FileNotFoundException e) {
    		return StatusCode.NOT_FOUND;
        }
    	catch (Exception e){
    		return StatusCode.EXCEPTION;
    	}
    }
    
    public StatusCode writeToFile(String filePath) {
    	Gson gson = new Gson();
    	System.out.println("DEBUG: playlistList = " + playlistList);
    	String json = gson.toJson(playlistList);
    	
    	try {
    		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    		bw.write(json);
    		bw.close();
            return StatusCode.SUCCESS;
            
    	} catch (IOException e) {
            return StatusCode.EXCEPTION;
        }
    }
    
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
    
    /*package*/ StatusCode displayStats(String playlistName) {  	
    	if (playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
    	for (Playlist playlist : playlistList) {
    		if (playlist.getPlaylistName().equals(playlistName)) {
    			
    			System.out.println("---------------------------");
    			
    			System.out.println("Playlist name: " + playlist.getPlaylistName());
    			System.out.println("Author: " + playlist.getAuthor());
    			
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
    				System.out.println(" * " + song.getSongName() + " - " + song.getArtistName());
    			}
    			System.out.println("---------------------------");
    			System.out.println();

    			return StatusCode.SUCCESS;
    		}
    	}
    	//System.out.println("Sorry, the playlist you entered could not be found.");
   		return StatusCode.NOT_FOUND;
    }
    
    private StatusCode addNewPlaylist(String playlistName) {
    	if(playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
         ArrayList<Song> songs = new ArrayList<Song>();
         
         Playlist newPlaylist = new Playlist(Main.username, playlistName, songs);
         
         for(Playlist playlist : playlistList)
         {
        	 if(playlist.getPlaylistName() == newPlaylist.getPlaylistName())
        	 {
                 System.out.println("Sorry that playlist already exists");
                 return StatusCode.INVALID_INPUT;
        	 }
         }
  
         playlistList.add(newPlaylist);
         System.out.println("Playlist created successfully!!");
         return StatusCode.SUCCESS;
    }
    
    private StatusCode deletePlaylist(String playlistName) {
        if(playlistName == null)
        	return StatusCode.INVALID_INPUT;
    	
    	
        // TEST CODE: DELETE AFTER IMPLEMENTATION
        System.out.println("Playlist deleted");
        // END TEST CODE
           
        return StatusCode.FAILURE;
    }
    
    private StatusCode editPlaylist(String playlistName) {
    	if(playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
    	for(Playlist playlist : playlistList) {
    		if(playlist.getPlaylistName().equals(playlistName)) {
    			return playlist.editPlaylist();
    		}
    	}
    		
        return StatusCode.NOT_FOUND;
       
    }
    
    public ArrayList<Song> searchSongsBySongName(String songName) {
        ArrayList<Song> locatedSong = new ArrayList<>();
        for (Playlist playlist : playlistList) {
            for (Song song : playlist.getSongs()) {
                if (song.getSongName().equalsIgnoreCase(songName)) {
                    locatedSong.add(song);
                }
            }
        }
        if (!locatedSong.isEmpty()) {
            System.out.println("Search for song '" + songName + "' was successful.");
        } else {
            System.out.println("No songs found matching the name '" + songName + "'.");
        }
      
        return locatedSong;
    }
    
    public void sortPlaylistAlphabetically() {
        for (int i = 1; i < playlistList.size(); i++) {
            Playlist currentPlaylist = playlistList.get(i);
            String currentSongName = currentPlaylist.getSongs().isEmpty() ? "" : currentPlaylist.getSongs().get(0).getSongName();
            int j = i - 1;
            while (j >= 0 && compare(playlistList.get(j), currentSongName) > 0) {
                playlistList.set(j + 1, playlistList.get(j));
                j--;
            }
            playlistList.set(j + 1, currentPlaylist);
        }
    }
    private int compare(Playlist playlist, String songName) {
        String playlistSongName = playlist.getSongs().isEmpty() ? "" : playlist.getSongs().get(0).getSongName();
        return playlistSongName.compareToIgnoreCase(songName);
    }

 
    
    
    public StatusCode viewPlaylists()
    {
    	System.out.println("Here are your playlists:");
    	if(playlistList.size() < 1)
    	{
    		System.out.println("You have no playlists");
    		return StatusCode.SUCCESS;
    	}
    	for(Playlist playlist : this.playlistList)
    	{
    		System.out.println(" * " + playlist.getPlaylistName());
    	}
    	return StatusCode.SUCCESS;
    }
    
    private StatusCode searchSongs() {
    	System.out.println("Search by...");
    	System.out.println("1 - Song Name");
    	System.out.println("2 - Author Name");
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
			return searchByName();
		case 2:
			return searchByAuthor();
		case 3:
			return searchByLength();
		default:
			return StatusCode.INVALID_INPUT;
		}
    }
    
    private StatusCode searchByName() {
    	return StatusCode.NOT_IMPLEMENTED;
    }
    
    private StatusCode searchByAuthor() {
    	return StatusCode.NOT_IMPLEMENTED;
    }
    
    private StatusCode searchByLength() {
    	return StatusCode.NOT_IMPLEMENTED;
    }
    
    /**
     * The main operating logic of the program. Will continually ask the user what they would like to do.
     * @return SUCCESS or FAILURE or EXCEPTION or NOT_FOUND
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
			System.exit(0);
			return StatusCode.SUCCESS;
		default:
			return StatusCode.INVALID_INPUT;
		}
    }
    
    public StatusCode printMainMenu() {
    	System.out.println("1 - Add a new playlist");
    	System.out.println("2 - Delete a playlist");
    	System.out.println("3 - Edit a playlist");
    	System.out.println("4 - View a playlist");
    	System.out.println("5 - View all my playlists");
    	System.out.println("6 - Search my songs");
    	System.out.println("7 - View social menu");
    	System.out.println("8 - Exit");
    	System.out.print("What would you like to do?");
    	return StatusCode.SUCCESS;
    }
    
    /**
     * 
     * @return an integer between 1-6 indicating the users selection. Or -1 upon failure.
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