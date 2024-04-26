package playlists;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import program.Main;

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
	
    private void readFromFile(String authorName) {
    	Gson gson = new Gson();
    	
    	String filePath = authorName + ".json";
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(filePath));
    		
    		TypeToken<ArrayList<Playlist>> playlistListType = new TypeToken<ArrayList<Playlist>>() {};

    		playlistList = gson.fromJson(br, playlistListType);

          } catch (IOException e) {
          e.printStackTrace();
          }
 
    }
    
    private void writeToFile(String authorName) {
    	Gson gson = new Gson();
    	String json = gson.toJson(playlistList);
    	String filePath = authorName + ".json";
    	try {
    		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    		bw.write(json);
    		bw.close();
            System.out.println("Playlist saved successfully!");
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void displayStats() {
    	//ask the user which playlist they would like to view
    	System.out.println("Which playlist would you like to view stats for?");
    	Scanner scanner = new Scanner(System.in);
    	String playlistName = scanner.nextLine().trim();
    	
    	//This variable will determine whether or not the specified playlist has been found
    	Boolean isFound = false;
    	
    	for (Playlist playlist : playlistList) {
    		if (playlist.getPlaylistName() == playlistName) {
    			isFound = true;
    			
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
    		}
    	}
    	
    	if(!isFound) {
    		System.out.println("Sorry, the playlist you entered could not be found.");
    	}
    }
    
    private void addNewPlaylist() {
    	 Scanner scanner = new Scanner(System.in);
         System.out.println("Please enter the name of the your new playlist:");
         String playlistName = scanner.nextLine();
         
         ArrayList<Song> songs = new ArrayList<Song>();
         
         Playlist newPlaylist = new Playlist(Main.username, playlistName, songs);
         
         for(Playlist playlist : playlistList)
         {
        	 if(playlist.getPlaylistName() == newPlaylist.getPlaylistName())
        	 {
                 System.out.println("Sorry that playlist already exists");
                 return;
        	 }
         }
  
         playlistList.add(newPlaylist);
         //this.writeToFile(Main.username);
         System.out.println("Playlist created successfully!!");
    }
    
    private void deletePlaylist() {
    	   Scanner scanner = new Scanner(System.in);
           System.out.println("Please enter the name of the playlist you want to delete:");
           
           // TEST CODE: DELETE AFTER IMPLEMENTATION
           System.out.println("Playlist deleted");
           // END TEST CODE
   	
    }
    
    private void editPlaylist() {
    	Scanner scanner = new Scanner(System.in);
    	
    	// Ask the user the name of the playlist they want to edit
    	System.out.println("What playlist would you like to edit?");
    	String playlistName = scanner.nextLine();
    	
    	//use boolean to keep track of whether the playlist exists
    	boolean found = false;
    	for(Playlist playlist : playlistList) {
    		if(playlist.getPlaylistName() == playlistName) {
    			found = true;
    			playlist.editPlaylist();
    			break;
    		}
    	}
    		
    	if (found == false) { //playlist does not exist
            System.out.println("Sorry, that playlist does not exist.");
            return;
        }
    }
    
    private void viewPlaylists()
    {
    	System.out.println("Here are your playlists:");
    	if(playlistList.size() < 1)
    	{
    		System.out.println("You have no playlists");
    		return;
    	}
    	for(Playlist playlist : this.playlistList)
    	{
    		System.out.println(playlist.getPlaylistName());
    	}
    }
    
    public void choiceMenu() {
    	System.out.println("1 - Add a new playlist");
    	System.out.println("2 - Delete a playlist");
    	System.out.println("3 - Edit a playlist");
    	System.out.println("4 - View a playlist");
    	System.out.println("5 - View all my playlists");
    	System.out.println("6 - Exit");
    	System.out.print("What would you like to do?");
    	
    	int userSelection = 0;
		Scanner scanner = new Scanner(System.in);
		while(true) 
		{
			try 
			{
				userSelection = scanner.nextInt();
				if (userSelection < 1 || userSelection > 6)
					throw new Exception();
				break;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Please enter a number 1-6.");
				continue;
			}
		}
		
		switch (userSelection) {
		case 1:
			addNewPlaylist();
			writeToFile(Main.username);
			break;
		case 2:
			deletePlaylist();
			writeToFile(Main.username);
			break;
		case 3:
			editPlaylist();
			writeToFile(Main.username);
			break;
		case 4:
			displayStats();
			break;
		case 5:
			viewPlaylists();
			break;
		case 6:
			writeToFile(Main.username);
			System.exit(0);
			break;
		}
    }
    
    
}