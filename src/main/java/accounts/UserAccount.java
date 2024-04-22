package accounts;

import java.util.ArrayList;

import playlists.Playlist;

public class UserAccount {
	private String username;
	private String password;
	private ArrayList<Playlist> playlists;
	
	public UserAccount(String username, String password, ArrayList<Playlist> playlists) {
		super();
		this.username = username;
		this.password = password;
		this.playlists = playlists;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(ArrayList<Playlist> playlists) {
		this.playlists = playlists;
	}
	
	
}


