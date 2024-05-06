package playlistsTests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import playlists.Playlist;
import playlists.Song;
import program.StatusCode;

public class PlaylistTest {
	
	Playlist playlist;

	@BeforeEach
	void setUp() {
		playlist = new Playlist("testAuthor", "testPlaylist", new ArrayList<Song>());
	}
	
	@Test
	void testRenamePlaylistWithNull() {
		assertEquals(StatusCode.INVALID_INPUT, playlist.renamePlaylist(null));
	}
	
	@Test
	void testRenamePlaylistWithValid() {
		assertEquals(StatusCode.SUCCESS, playlist.renamePlaylist("testPlaylist"));
	}
	
	@Test
	void testGetAuthor() {
		assertEquals("testAuthor", playlist.getAuthor());
	}
	
	@ParameterizedTest
	@MethodSource("provideStringForSetAuthor")
	public void testSetAuthor(String author, StatusCode expected) {
		assertEquals(expected, playlist.setAuthor(author));
	}
	
	public static Stream<Object[]> provideStringForSetAuthor() {
		
		return Stream.of(
			new Object[]{"testerAuthor", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testGetPlaylistName() {
		assertEquals("testPlaylist", playlist.getPlaylistName());
	}
	
	@ParameterizedTest
	@MethodSource("provideStringForSetPlaylistName")
	public void testSetPlaylistName(String playlistName, StatusCode expected) {
		assertEquals(expected, playlist.setPlaylistName(playlistName));
	}
	
	public static Stream<Object[]> provideStringForSetPlaylistName() {
		
		return Stream.of(
			new Object[]{"testerPlaylist", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testGetSumOfRatings() {
		assertEquals(0, playlist.getSumOfRatings());
	}
	
	@ParameterizedTest
	@MethodSource("provideIntForSetSumOfRatings")
	public void testSetSumOfRatings(int sum, StatusCode expected) {
		assertEquals(expected, playlist.setSumOfRatings(sum));
	}
	
	public static Stream<Object[]> provideIntForSetSumOfRatings() {
		
		return Stream.of(
			new Object[]{1, StatusCode.SUCCESS},
			new Object[]{0, StatusCode.SUCCESS},
			new Object[]{-1, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testGetNumOfRatings() {
		assertEquals(0, playlist.getNumOfRatings());
	}
	
	@ParameterizedTest
	@MethodSource("provideIntForSetNumOfRatings")
	public void testSetNumOfRatings(int num, StatusCode expected) {
		assertEquals(expected, playlist.setNumOfRatings(num));
	}
	
	public static Stream<Object[]> provideIntForSetNumOfRatings() {
		
		return Stream.of(
			new Object[]{1, StatusCode.SUCCESS},
			new Object[]{0, StatusCode.SUCCESS},
			new Object[]{-1, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	public void testAskSongName() {
		String input = "Song\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		String result = playlist.askSongName();
		
		assertTrue("Song".equals(result));
		System.setIn(System.in);
	}
	
	@Test
	public void testEditPlaylist_DoneOption() {
		//passing 0 first, to test invalid input as well
		String input = "0\n" + "6\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		assertEquals(StatusCode.SUCCESS, playlist.editPlaylist());
		System.setIn(System.in);
	}
	
	@ParameterizedTest
	@MethodSource("provideStringForAddSong")
	public void testAddSong(String songName, StatusCode expected) {
		assertEquals(expected, playlist.addSong(songName));
	}
	
	public static Stream<Object[]> provideStringForAddSong() {
		
		return Stream.of(
			new Object[]{"Itsy Bitsy Spider", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@ParameterizedTest
    @MethodSource("provideStringForRemoveSong")
    public void testRemoveSong(String songName, StatusCode expected) {
		Song song1 = new Song("ABC", "Mozart", 60);
		ArrayList<Song> testerSongs = new ArrayList<Song>();
		testerSongs.add(song1);
		
		playlist.setSongs(testerSongs);
		
        assertEquals(expected, playlist.removeSong(songName));
    }

    public static Stream<Object[]> provideStringForRemoveSong() {
        return Stream.of(
            new Object[]{"ABC", StatusCode.SUCCESS},
            new Object[]{"Itsy Bitsy Spider", StatusCode.NOT_FOUND},
            new Object[]{null, StatusCode.INVALID_INPUT}
        );
    }
    
	@Test
	void testToStringNoRatings() {
		assertEquals("\n" + playlist.getPlaylistName() + " - " + playlist.getAuthor() +
	            "\nNumber of songs: " + playlist.getSongs().size() + " Average rating: " + "0", playlist.toString());
	}
	
	@Test
	void testToStringRatings() {
		
		playlist.setNumOfRatings(4);
		playlist.setSumOfRatings(16);
		
		double average = playlist.getSumOfRatings() / playlist.getNumOfRatings();
		
		
		assertEquals(("\n" + playlist.getPlaylistName() + " - " + playlist.getAuthor() +
	            "\nNumber of songs: " + playlist.getSongs().size() + " Average rating: " + average), playlist.toString());
	}
}