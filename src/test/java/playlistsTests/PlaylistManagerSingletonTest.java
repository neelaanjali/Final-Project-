package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
