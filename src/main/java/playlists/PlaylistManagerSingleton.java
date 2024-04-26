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
	public ArrayList<Playlist> playlistList;
	
	public PlaylistManagerSingleton() {
		this.playlistList = new ArrayList<Playlist>();
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
    
    private StatusCode writeToFile(String filePath) {
    	Gson gson = new Gson();
    	String json = gson.toJson(playlistList);
    	
    	try {
    		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    		bw.write(json);
    		bw.close();
            System.out.println("Playlist saved successfully!");
            return StatusCode.SUCCESS;
            
    	} catch (IOException e) {
            return StatusCode.EXCEPTION;
        }
    }
    
    private String askPlaylistName() {
    	System.out.println("What is the name of the playlist?");
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
    
    private StatusCode displayStats(String playlistName) {  	
    	if (playlistName == null)
    		return StatusCode.INVALID_INPUT;
    	
    	for (Playlist playlist : playlistList) {
    		if (playlist.getPlaylistName().equals(playlistName)) {
    			
    			System.out.println("Playlist name: " + playlist.getPlaylistName());
    			System.out.println("Author: " + playlist.getAuthor());
    			
    			int totalSeconds = 0;
    			for (Song song : playlist.getSongs()) {
    				totalSeconds += song.getLength();
    			}
    			
    			Integer totalMinutes = totalSeconds / 60;
    			Integer seconds = totalSeconds % 60;
    			String totalTime = totalMinutes.toString() + ":" + seconds.toString();
    			
    			System.out.println("Total Length: " + totalTime);
    			System.out.println("Number of Songs: " + playlist.getSongs().size());
    			
    			for (Song song : playlist.getSongs()) {
    				System.out.println(song.getSongName());
    			}
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
    		if(playlist.getPlaylistName() == playlistName) {
    			return playlist.editPlaylist();
    		}
    	}
    		
        System.out.println("Sorry, that playlist does not exist.");
        return StatusCode.NOT_FOUND;
       
    }
    
    private StatusCode viewPlaylists()
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
			writeToFile(Main.username + ".json");
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
    	System.out.println("6 - Exit");
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
			if (userSelection < 1 || userSelection > 6)
			{
				System.out.println("Enter a number between 1 and 6: ");
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