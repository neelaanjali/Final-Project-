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

import songs.Song;

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
	
    public void readFromFile(String authorName) {
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
    
    public void writeToFile(String authorName) {
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
    
    public void displayStats(String playlistName) {
    	for (Playlist playlist : playlistList) {
    		if (playlist.getPlaylistName() == playlistName) {
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
    }
}