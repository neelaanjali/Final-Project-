package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
}
