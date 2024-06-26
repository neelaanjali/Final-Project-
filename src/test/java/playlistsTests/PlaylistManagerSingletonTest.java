package playlistsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import playlists.Playlist;
import playlists.PlaylistManagerSingleton;
import playlists.Song;
import program.Main;
import program.StatusCode;

public class PlaylistManagerSingletonTest {
	
	private PlaylistManagerSingleton manager;
	private Playlist playlist;
	
	@BeforeEach
	private void setUp() {
		manager = PlaylistManagerSingleton.getInstance();
		
		playlist = new Playlist("testAuthor", "testPlaylist", new ArrayList<Song>());	
	}
	
	@Test
	private void testViewPlaylists() {
		assertEquals(StatusCode.SUCCESS, manager.viewPlaylists());
	}
	
	/**
	 * @author riannaellis
	 */
	@ParameterizedTest
	@MethodSource("provideFilePathForReadFromFile")
	public void readFromFile(String filePath, StatusCode expected) {
		assertEquals(expected, manager.readFromFile(filePath));
	}
	
	public static Stream<Object[]> provideFilePathForReadFromFile() {
		
		return Stream.of(
			new Object[]{"test.json", StatusCode.SUCCESS},
			new Object[]{"tess.json", StatusCode.NOT_FOUND},
			new Object[]{"emptyTestCase.json", StatusCode.EXCEPTION}
		);
	}
	
	/**
	 * @author riannaellis
	 */
	@ParameterizedTest
	@MethodSource("provideFilePathForWriteToFile")
	public void testWriteToFile(String filePath, StatusCode expected) {
		Playlist playlist1 = new Playlist("Author1", "Name1", new ArrayList<Song>());
		
		Song song1 = new Song("ABC", "Mozart", 60);
		ArrayList<Song> testerSongs = new ArrayList<Song>();
		testerSongs.add(song1);
		
		playlist1.setSongs(testerSongs);
		
		manager.addNewPlaylist("Name1");
		
		assertEquals(expected, manager.writeToFile(filePath));
	}
	
	public static Stream<Object[]> provideFilePathForWriteToFile() {
		
		return Stream.of(
			new Object[]{"writeTestCase.json", StatusCode.SUCCESS},
			new Object[]{"/path/to/nonexistent/tester.json", StatusCode.EXCEPTION}
		);
	}
	
	/**
	 * @author riannaellis
	 */
	@ParameterizedTest
	@MethodSource("providePlaylistForDisplayStats")
	public void testDisplayStats(String playlistName, StatusCode expected) {
		manager.readFromFile("myusername.json");
		assertEquals(expected, manager.displayStats(playlistName));
	}
	
	public static Stream<Object[]> providePlaylistForDisplayStats() {
		
		return Stream.of(
			new Object[]{"myplaylist", StatusCode.SUCCESS},
			new Object[]{"fakePlaylist", StatusCode.NOT_FOUND},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	/**
	 * @author riannaellis
	 */
	@ParameterizedTest
	@MethodSource("provideStringForAddNewPlaylist")
	public void testAddNewPlaylist(String playlistName, StatusCode expected) {
		
		manager.readFromFile("myusername.json");
		assertEquals(expected, manager.addNewPlaylist(playlistName));
	}
	
	public static Stream<Object[]> provideStringForAddNewPlaylist() {
		
		return Stream.of(
			new Object[]{"newPlaylist", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	
	@ParameterizedTest
	@MethodSource("provideStringForDeletePlaylist")
	  public void testDeletePlaylist(String playlistName, StatusCode expected) {
	        PlaylistManagerSingleton manager = PlaylistManagerSingleton.getInstance();
	        manager.readFromFile("myusername.json");
	        assertEquals(expected, manager.deletePlaylist(playlistName));
	    }

	    public static Stream<Object[]> provideStringForDeletePlaylist() {
	        return Stream.of(
	            new Object[]{"existingPlaylist", StatusCode.FAILURE},
	            new Object[]{null, StatusCode.INVALID_INPUT}
	        );
	    }

	/**
	* @author riannaellis
	*/
	@ParameterizedTest
	@MethodSource("provideStringForEditPlaylist")
	public void testEditPlaylist(String playlistName, StatusCode expected) {
		manager.readFromFile("myusername.json");
		assertEquals(expected, manager.editPlaylist(playlistName));
	}
	
	public static Stream<Object[]> provideStringForEditPlaylist() {
		
		return Stream.of(
			new Object[]{"myplaylist", StatusCode.SUCCESS},
			new Object[]{"fakePlaylist", StatusCode.NOT_FOUND},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	public void testSearchByArtist() {
		String input = "Mozart";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		ArrayList<Song> foundSongs = manager.searchByArtist();
		ArrayList<Song> expected = new ArrayList<Song>();
		expected.add(new Song("Cherry", "Lana Del Rey", 255));
		
		assertEquals(expected.size(), foundSongs.size());
		
		for (Song expectedSong : expected) {
			boolean found=false;
			for (Song foundSong : foundSongs) { 
				if(expectedSong.getSongName().equals(foundSong.getSongName()) && 
				   expectedSong.getArtistName().equals(foundSong.getArtistName())) {
					found=true;
					break;
				}
			}
			assertTrue(found, "Expected song not found: " + expectedSong);
		}
	}
	
	@Test 
	public void testSearchByLength() {
		StatusCode result = manager.searchByLength();
		assertEquals(StatusCode.SUCCESS, result);
	}
	
}