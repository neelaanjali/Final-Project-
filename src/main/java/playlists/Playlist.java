package playlists;

/**
 * @author hargu
 * 
 * TODO:
 * 		uncomment the songs field after Song class has been created
 */
public class Playlist {
	//private ArrayList<Song> songs;
	private String author;
	private String playlistName;
	
//	public ArrayList<Song> getSongs() {
//		return songs;
//	}
//	public void setSongs(ArrayList<Song> songs) {
//		this.songs = songs;
//	}
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