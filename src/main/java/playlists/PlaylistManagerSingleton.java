package playlists;

import com.google.gson.*;
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
}