package playlistsTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import playlists.Song;
import program.StatusCode;

public class SongTest {
	Song song;

	@BeforeEach
	void setUp() {
		song = new Song("ABC", "Michael Jackson", 60);
	}
	
	@Test
	void testCopyConstructor() {
		Song newSong = new Song(song);
		
		assertTrue(newSong.getSongName().equals("ABC"));
		assertTrue(newSong.getArtistName().equals("Michael Jackson"));
		assertEquals(60, newSong.getLength());
		assertTrue(!newSong.equals(song));
	}
	
	@Test
	void testGetSongName() {
		assertEquals("ABC", song.getSongName());
	}
	
	@ParameterizedTest
	@MethodSource("provideStringForSetSongName")
	public void testSetSongName(String songName, StatusCode expected) {
		assertEquals(expected, song.setSongName(songName));
	}
	
	public static Stream<Object[]> provideStringForSetSongName() {
		
		return Stream.of(
			new Object[]{"Twinkle, Twinkle", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testGetArtistName() {
		assertEquals("Michael Jackson", song.getArtistName());
	}
	
	@ParameterizedTest
	@MethodSource("provideStringForSetArtistName")
	public void testSetArtistName(String artistName, StatusCode expected) {
		assertEquals(expected, song.setArtistName(artistName));
	}
	
	public static Stream<Object[]> provideStringForSetArtistName() {
		
		return Stream.of(
			new Object[]{"Ariana Grande", StatusCode.SUCCESS},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testGetLength() {
		assertEquals(60, song.getLength());
	}
	
	@ParameterizedTest
	@MethodSource("provideIntForSetLength")
	public void testSetLength(int length, StatusCode expected) {
		assertEquals(expected, song.setLength(length));
	}
	
	public static Stream<Object[]> provideIntForSetLength() {
		
		return Stream.of(
			new Object[]{1, StatusCode.SUCCESS},
			new Object[]{0, StatusCode.INVALID_INPUT},
			new Object[]{-1, StatusCode.INVALID_INPUT},
			new Object[]{null, StatusCode.INVALID_INPUT}
		);
	}
	
	@Test
	void testToStringLessThan10Secs() {
		assertEquals("ABC - Michael Jackson 1:00", song.toString());
	}
	
	@Test
	void testToStringMoreThan10Secs() {
		song.setLength(78);
		
		assertEquals("ABC - Michael Jackson 1:18", song.toString());
	}
	
}
