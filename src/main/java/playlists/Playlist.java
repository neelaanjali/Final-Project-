package playlists;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author hargu
 * 
 */
public class Playlist {
	private ArrayList<Song> songs;
	private String author;
	private String playlistName;
	
	/**
	 * @author riannaellis
	 * 
	 */	
	public Playlist(String author, String playlistName, ArrayList<Song> songs) {
		super();
		this.songs = songs;
		this.author = author;
		this.playlistName = playlistName;
	}
	
	/*
	 * @author jxie26
	 * 
	 */
	public void editPlaylist()
	{
		//ask the user if they want to add or remove a song
		System.out.println("How would you like to edit your playlist?");
		System.out.println("1 - Add a new song");
		System.out.println("2 - Delete a song");
		System.out.println("3 - Done editing");
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				int response = scanner.nextInt();
				
				if (response == 1) {
					this.addSong();
					break;
				}
				else if (response == 2) {
					this.removeSong();
					break;
				}
				else if (response == 3) {
					return;
				}
				else { //invalid user input
					System.out.println("Please insput 1, 2 or 3.");
					continue;
				}
			}
			catch (Exception e){
				e.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * @author riannaellis
	 * 
	 */
	private void addSong() {
		Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the name of the song
        System.out.print("Enter the song name: ");
        // Read the song name
        String songName = scanner.nextLine();

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

        //scanner.close();
	}	
	
	/**
	 * @author riannaellis
	 * 
	 */
	private void removeSong() {
		Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the name of the song they want to delete
        System.out.print("Enter the song name you'd like to delete: ");
        // Read the song name
        String songName = scanner.nextLine();	
		
		for (Song song : songs) {
			// If the song equals the target song, it removes it
            if (song.getSongName() == songName) {
                songs.remove(song);
            }
        }
	}
	
	public ArrayList<Song> getSongs() {
		return songs;
	}
	public void setSongs(ArrayList<Song> songs) {
		this.songs = songs;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public static void add(String playlistName2) {
		// TODO Auto-generated method stub
		
	}
}
