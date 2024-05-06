package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
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
	
	@ParameterizedTest
	@MethodSource("provideFilePathForReadFromFile")
	public void readFromFile(String filePath, StatusCode expected) {
		assertEquals(expected, manager.readFromFile(filePath));
	}
	
	public static Stream<Object[]> provideFilePathForReadFromFile() {
		
		return Stream.of(
			new Object[]{"test.json", StatusCode.SUCCESS},
			new Object[]{"tess.json", StatusCode.NOT_FOUND},
			new Object[]{"emptyTest.json", StatusCode.EXCEPTION}
		);
	}
	
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
			new Object[]{"test.json", StatusCode.SUCCESS},
			new Object[]{"/path/to/nonexistent/test.json", StatusCode.EXCEPTION}
		);
	}
	
	@ParameterizedTest
	@MethodSource("providePlaylistForDisplayStats")
	public void testDisplayStats(String playlistName, StatusCode expected) {
		

		
		assertEquals(expected, manager.displayStats(playlistName));
	}
	
	public static Stream<Object[]> providePlaylistForDisplayStats() {
		
		return Stream.of(
			new Object[]{"Test Playlist", StatusCode.SUCCESS},
			new Object[]{"fakePlaylist", StatusCode.NOT_FOUND},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	
	@Test
	public void testSearchSongsByArtistNotFound() {
		String inputArtist = "notAnArtist\n";
		InputStream inS = new ByteArrayInputStream(inputArtist.getBytes());
		System.setIn(inS);
		
		StatusCode result = manager.searchByArtist();
		System.setIn(System.in);
		
		assertEquals(StatusCode.NOT_FOUND, result);
	}
			
	@Test
	public void testSearchSongsByLength() {
		String inputLength = "5:37\n";
		InputStream inS = new ByteArrayInputStream(inputLength.getBytes());
		System.setIn(inS);
		
		StatusCode result = manager.searchByLength();
		System.setIn(System.in);
		
		assertEquals(StatusCode.NOT_FOUND, result);
	}
		
	}