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
import program.StatusCode;

public class PlaylistManagerSingletonTest {
	
	private PlaylistManagerSingleton manager;
	
	@BeforeEach
	private void setUp() {
		manager = PlaylistManagerSingleton.getInstance();
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
}