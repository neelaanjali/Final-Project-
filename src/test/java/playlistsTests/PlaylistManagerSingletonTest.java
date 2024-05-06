package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	@BeforeEach
	private void setUp() {
		manager = PlaylistManagerSingleton.getInstance();
		
		Song song1 = new Song("ABC", "Michael Jackson", 60);
		Song song2 = new Song("Twinkle, Twinkle", "Mozart", 120);
		Song song3 = new Song("Hot Cross Buns", "Ariana Grande", 30);
		
		ArrayList<Song> testerSongs = new ArrayList<Song>();
		testerSongs.add(song1);
		testerSongs.add(song2);
		testerSongs.add(song3);
		
		Playlist playlist = new Playlist("testAuthor", "testPlaylist", testerSongs);	
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
			new Object[]{"testPlaylist", StatusCode.SUCCESS},
			new Object[]{"fakePlaylist", StatusCode.NOT_FOUND}
		);
	}
}