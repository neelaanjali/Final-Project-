package playlists;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

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
 
//        String filename = authorName + ".csv";
//        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                String playlistName = parts[0];
//                String author = parts[1];
//                Playlist playlist = new Playlist(playlistName, author);
//                for (int i = 2; i < parts.length; i += 3) {
//                    String songName = parts[i];
//                    String artistName = parts[i + 1];
//                    String length = parts[i + 2];
//                    Song song = new Song(songName, artistName, length);
//                    playlist.addSong(song);
//                }
//                playlists.add(playlist);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
    	
    }
    
    private void deletePlaylist() {
    	
    }
    
    private void editPlaylist() {
    	// Ask the user the name of the playlist they want to edit
    	// Then do playlist.editPlaylist() to edit the specfic playlist object
    }
    
    public void choiceMenu() {
    	System.out.println("1 - Add a new playlist");
    	System.out.println("2 - Delete a playlist");
    	System.out.println("3 - Edit a playlist");
    	System.out.println("4 - View a playlist");
    	System.out.println("5 - Exit");
    	System.out.print("What would you like to do?");
    	
    	int userSelection = 0;
		Scanner scanner = new Scanner(System.in);
		while(true) 
		{
			try 
			{
				userSelection = scanner.nextInt();
				if (userSelection < 1 || userSelection > 5)
					throw new Exception();
				break;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Please enter a number 1-5.");
				continue;
			}
		}
		
		switch (userSelection) {
		case 1:
			addNewPlaylist();
		case 2:
			deletePlaylist();
		case 3:
			editPlaylist();
		case 4:
			displayStats();
		case 5:
			System.exit(0);
		}
    }
    
    
    public void AddNewPlaylist() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the name of the your new playlist:");
        String playlistName = scanner.nextLine();
        if (playlistList.contains(playlistName)) {
            System.out.println("Sorry that playlist already exists");
        } else {
            Playlist.add(playlistName);
            System.out.println("Playlist created successfully!!");
        }
        scanner.close();
       }
}