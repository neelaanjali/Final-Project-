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
	
	private PlaylistManagerSingleton() {
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
    
    public StatusCode writeToFile(String filePath) {
    	Gson gson = new Gson();
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
    
    /*package*/ public StatusCode displayStats(String playlistName) {  	
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
    				System.out.println(" * " + song.toString());
    			}
    			System.out.println("---------------------------");
    			System.out.println();

    			return StatusCode.SUCCESS;
    		}
    	}
    	//System.out.println("Sorry, the playlist you entered could not be found.");
   		return StatusCode.NOT_FOUND;
    }
    
    public StatusCode addNewPlaylist(String playlistName) {
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
    			StatusCode result = playlist.editPlaylist();
    			writeToFile(Main.username + ".json");
    			return result;
    		}
    	}	
        return StatusCode.NOT_FOUND;
    }
        
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
    /*public static void testSearchSongsBySongName(PlaylistManagerSingleton playlistManager, String songName) {
    System.out.println("Searching for song: " + songName);
    ArrayList<Song> locatedSongs = playlistManager.searchSongsBySongName(songName);
    if (!locatedSongs.isEmpty()) {
        System.out.println("Located songs:");
        for (Song song : locatedSongs) {
            System.out.println(song.getSongName() + " by " + song.getArtistName());
        }
    } else {
        System.out.println("No songs found matching the name '" + songName + "'.");
    }
    System.out.println();
}
*/
    
    //moved to the correct class. here for reference.
    //also had to change the logic bc it was not working
    ///////////////////////////////////////////////////////////////////////////////
//    
//    public void sortPlaylistAlphabetically() {
//        for (int i = 1; i < playlistList.size(); i++) {
//            Playlist currentPlaylist = playlistList.get(i);
//            String currentSongName = currentPlaylist.getSongs().isEmpty() ? "" : currentPlaylist.getSongs().get(0).getSongName();
//            int j = i - 1;
//            while (j >= 0 && compare(playlistList.get(j), currentSongName) > 0) {
//                playlistList.set(j + 1, playlistList.get(j));
//                j--;
//            }
//            playlistList.set(j + 1, currentPlaylist);
//        }
//    }
//    
//    private int compare(Playlist playlist, String songName) {
//        String playlistSongName = playlist.getSongs().isEmpty() ? "" : playlist.getSongs().get(0).getSongName();
//        return playlistSongName.compareToIgnoreCase(songName);
//    }
//    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
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
			return searchByArtist();
		case 3:
			return searchByLength();
		default:
			return StatusCode.INVALID_INPUT;
		}
    }
    
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
    
    public StatusCode searchByArtist() {
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
                for (Song song : foundSongs) {
                	System.out.println(song.toString());
                }
            } 
            else {
                System.out.println("No songs were found by the artist '" + artist + "'.");
                return StatusCode.NOT_FOUND;
            }
            return StatusCode.SUCCESS;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return StatusCode.EXCEPTION;
    	}
    }
    
    public StatusCode searchByLength() {
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