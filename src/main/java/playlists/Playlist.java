package playlists;

import java.util.ArrayList;
import java.util.Scanner;

import songs.Song;

/**
 * @author hargu
 * 
 */
public class Playlist {
	private ArrayList<Song> songs;
	private String author;
	private String playlistName;
	
	public Playlist(String author, String playlistName, ArrayList<Song> songs) {
		super();
		this.songs = songs;
		this.author = author;
		this.playlistName = playlistName;
	}
	
	public void addSong() {
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
        //Parse length to int
        String[] time = stringLength.split(":");
        int length = (Integer.parseInt(time[0]) * 60) + Integer.parseInt(time[1]);
        
        // Create a new Song instance with the input
        Song newSong = new Song(songName, artist, length);
        
        // Add the song to the playlist
        songs.add(newSong);

        // Don't forget to close the Scanner object to prevent resource leaks
        scanner.close();
	}	
	
	public void removeSong() {
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
}
