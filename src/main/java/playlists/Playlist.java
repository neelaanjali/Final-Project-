package playlists;

import java.util.ArrayList;
import java.util.Scanner;

import program.StatusCode;

/**
 * @author hargu
 * 
 */
public class Playlist {
	private ArrayList<Song> songs;
	private String author;
	private String playlistName;
	private int sumOfRatings;
	private int numOfRatings;
	
	/**
	 * @author riannaellis
	 * 
	 */	
	public Playlist(String author, String playlistName, ArrayList<Song> songs) {
		super();
		this.songs = songs;
		this.author = author;
		this.playlistName = playlistName;
		this.sumOfRatings = 0;
		this.numOfRatings = 0;
	}
	
	/*
	 * @author jxie26
	 * 
	 */
	public StatusCode editPlaylist()
	{
		//ask the user if they want to add or remove a song
		System.out.println("How would you like to edit your playlist?");
		System.out.println("1 - Add a new song");
		System.out.println("2 - Delete a song");
		System.out.println("3 - Rename playlist");
		System.out.println("4 - Sort playlist by song title");
		System.out.println("5 - Sort playlist by song length");
		System.out.println("6 - Done editing");
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				int response = scanner.nextInt();
				
				if (response == 1) {
					return addSong(askSongName());
				}
				else if (response == 2) {
					return removeSong(askSongName());
				}
				else if (response == 3) {
					return renamePlaylist(PlaylistManagerSingleton.getInstance().askPlaylistName());
				}
				else if (response == 4) {
					return sortPlaylistByName();
				}
				else if (response == 5) {
					return sortPlaylistByLength();
				}
				else if (response == 6) {
					return StatusCode.SUCCESS;
				}
				else { //invalid user input
					System.out.println("Please input a number 1-6.");
					continue;
				}
			}
			catch (Exception e){
				return StatusCode.EXCEPTION;
			}
		}
	}
	
	private StatusCode sortPlaylistByName() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	private StatusCode sortPlaylistByLength() {
		return StatusCode.NOT_IMPLEMENTED;
	}
	
	public StatusCode renamePlaylist(String newName) {
		if(newName == null) 
			return StatusCode.INVALID_INPUT;
		
		this.playlistName = newName;
		return StatusCode.SUCCESS;
	}
	
	private String askSongName() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter the song name: ");
		String songName = scanner.nextLine();
		
		return songName;
	}
	
	/**
	 * @author riannaellis
	 * @return INVALID_INPUT or SUCCESS
	 */
	private StatusCode addSong(String songName) {
		if(songName == null)
			return StatusCode.INVALID_INPUT;
		
		Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the artist
        System.out.print("Enter the artist: ");
        // Read the artist name
        String artist = scanner.nextLine();
        
        // Prompt the user to enter the length
        System.out.print("Enter the length (MM:SS format): ");
        // Read the song name
        String stringLength = scanner.nextLine();
        // Parse length to int
        String[] time = stringLength.split(":");
        int length = (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
        
        // Create a new Song instance with the input
        Song newSong = new Song(songName, artist, length);
        
        // Add the song to the playlist
        songs.add(newSong);
        
        return StatusCode.SUCCESS;
	}	
	
	/**
	 * @author riannaellis
	 * @return INVALID_INPUT or SUCCESS
	 */
	private StatusCode removeSong(String songName) {
		if(songName == null)
			return StatusCode.INVALID_INPUT;
		
		for (Song song : songs) {
			// If the song equals the target song, it removes it
            if (song.getSongName() == songName) {
                songs.remove(song);
                return StatusCode.SUCCESS;
            }
        }
		return StatusCode.NOT_FOUND;
	}
	
	public ArrayList<Song> getSongs() {
		return songs;
	}
	
	public StatusCode setSongs(ArrayList<Song> songs) {
	    if (songs != null) {
	        this.songs = songs;
	        return StatusCode.SUCCESS;
	    } else {
	    	return StatusCode.INVALID_INPUT;
	    }
	}
	
	public String getAuthor() {
		return author;
	}
	public StatusCode setAuthor(String author) {
		if (author != null) {
	        this.author = author;
	        return StatusCode.SUCCESS;
	    } else {
	    	return StatusCode.INVALID_INPUT;
	    }
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public StatusCode setPlaylistName(String playlistName) {
		if (playlistName != null) {
	        this.playlistName = playlistName;
	        return StatusCode.SUCCESS;
	    } else {
	    	return StatusCode.INVALID_INPUT;
	    }
	}

	public int getSumOfRatings() {
		return sumOfRatings;
	}

	public StatusCode setSumOfRatings(int sumOfRatings) {
		if (sumOfRatings >= 0) {
	        this.sumOfRatings = sumOfRatings;
	        return StatusCode.SUCCESS;
	    } else {
	    	return StatusCode.INVALID_INPUT;
	    }
	}

	public int getNumOfRatings() {
		return numOfRatings;
	}

	public StatusCode setNumOfRatings(int numOfRatings) {
		if (numOfRatings >= 0) {
	        this.numOfRatings = numOfRatings;
	        return StatusCode.SUCCESS;
	    } else {
	    	return StatusCode.INVALID_INPUT;
	    }
	}
}
