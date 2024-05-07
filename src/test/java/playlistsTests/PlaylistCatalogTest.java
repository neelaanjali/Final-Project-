package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playlists.Playlist;
import playlists.PlaylistCatalog;
import program.StatusCode;

public class PlaylistCatalogTest {
	private PlaylistCatalog catalog;

	@BeforeEach
	public void setUp() {
		catalog = new PlaylistCatalog();
	}
	
//	@Test
//	public void textExecuteMenuWith1() {
//		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(1));
//	}
//	
//	@Test
//	public void testExecuteMenuWith3() {
//		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(3));
//	}
//	
//	@Test
//	public void testExecuteMenuWith4() {
//		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(4));
//	}
	
	@Test
	public void testExecuteMenuWithDefault() {
		assertEquals(StatusCode.INVALID_INPUT, catalog.executeMenu(0));
	}
	
//	@Test
//	public void testSaveChanges() {
//		ArrayList<Playlist> playlists = catalog.deserializePlaylists(catalog.loadPlaylistFiles());
//		assertEquals(StatusCode.SUCCESS, catalog.saveChanges("test", playlists));
//	}
	
	@Test
	public void testViewAllPlaylists() {
		assertEquals(StatusCode.SUCCESS, catalog.viewAllPlaylists());
	}
	
	@Test
	public void testGetUserSelectionValid() {
		String input = "2\n"; // Simulating user input "2"
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		int selection = catalog.getMenuSelection();
		assertEquals(2, selection);
	}
	
	@Test
	public void testGetUserSelectionInvalid() {
		String input = "0\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		int selection = catalog.getMenuSelection();
		assertEquals(-1, selection);
	}
	
	@Test
	public void testPrintMenuWithInvalidInput() {
		String input = "0\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		StatusCode result = PlaylistCatalog.printMenu();
		assertEquals(StatusCode.INVALID_INPUT, result);
	}
	
	@Test
	public void testRatePlaylistWithNull() {
		StatusCode result = catalog.ratePlaylist(null);
		assertEquals(StatusCode.INVALID_INPUT, result);
	}
	
//	@Test
//	public void testRatePlaylistWithInvalid() {
//		StatusCode result = catalog.ratePlaylist("thisplaylistdoesnotexist");
//		assertEquals(StatusCode.NOT_FOUND, result);
//	}
	
	@Test
	public void testRatePlaylistWithValid() throws Exception {
		String input = "5\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		//save contents of file
		BufferedReader br = new BufferedReader(new FileReader("test.json"));
		String fileContents = br.readLine();
		br.close();
		
		StatusCode result = catalog.ratePlaylist("Playlist123");
		
		//replace file contents
		BufferedWriter bw = new BufferedWriter(new FileWriter("test.json"));
		bw.write(fileContents);
		
		assertEquals(StatusCode.SUCCESS, result);
	}
	
	@Test
	public void testViewTopPlaylists() {
		StatusCode result = catalog.viewTopPlaylists();
		assertEquals(StatusCode.SUCCESS, result);
	}
	
	@Test
	public void testViewTopUsers() {
		StatusCode result = catalog.viewTopUsers();
		assertEquals(StatusCode.SUCCESS, result);
	}
}
