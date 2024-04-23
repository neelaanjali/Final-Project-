package playlists;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import songs.Song;

public class PlaylistManagerSingleton {
	
	private static PlaylistManagerSingleton instance;
	public ArrayList<Song> playlist;
	
	public PlaylistManagerSingleton() {
		this.playlist = new ArrayList<Song>();
	}
	
	public static PlaylistManagerSingleton getInstance() {
		//create the instance if it doesn't exist yet
        if (instance == null) {
            instance = new PlaylistManagerSingleton();
        }
        return instance;
	}
    public void readFromFile(String authorName) {
        String filename = authorName + ".csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String playlistName = parts[0];
                String author = parts[1];
                Playlist playlist = new Playlist(playlistName, author);
                for (int i = 2; i < parts.length; i += 3) {
                    String songName = parts[i];
                    String artistName = parts[i + 1];
                    String length = parts[i + 2];
                    Song song = new Song(songName, artistName, length);
                    playlist.addSong(song);
                }
                playlists.add(playlist);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        playlist.add(newSong);

        // Don't forget to close the Scanner object to prevent resource leaks
        scanner.close();
	}	
	
	public void removeSong(Song removedSong) {
		for (Song song : playlist) {
			// If the song equals the target song, it removes it
            if (song.equals(removedSong)) {
                playlist.remove(song);
            }
        }
	}
}