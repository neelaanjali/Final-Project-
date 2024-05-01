package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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
	
	@Test
	public void textExecuteMenuWith1() {
		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(1));
	}
	
	@Test
	public void testExecuteMenuWith3() {
		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(3));
	}
	
	@Test
	public void testExecuteMenuWith4() {
		assertEquals(StatusCode.SUCCESS, catalog.executeMenu(4));
	}
	
	@Test
	public void testExecuteMenuWithDefault() {
		assertEquals(StatusCode.INVALID_INPUT, catalog.executeMenu(0));
	}
	
	@Test
	public void testSaveChanges() {
		ArrayList<Playlist> playlists = catalog.deserializePlaylists(catalog.loadPlaylistFiles());
		assertEquals(StatusCode.SUCCESS, catalog.saveChanges("test", playlists));
	}
	
	@Test
	public void testViewAllPlaylists() {
		assertEquals(StatusCode.SUCCESS, catalog.viewAllPlaylists());
	}
}
