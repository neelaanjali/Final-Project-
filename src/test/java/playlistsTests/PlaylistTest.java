package playlistsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import playlists.Playlist;
import playlists.Song;
import program.StatusCode;

public class PlaylistTest {
	
	private Playlist playlist;

	@BeforeEach
	private void setUp() {
		playlist = new Playlist("testAuthor", "testPlaylist", new ArrayList<Song>());
	}
	
	@Test
	private void testRenamePlaylistWithNull() {
		assertEquals(StatusCode.INVALID_INPUT, playlist.renamePlaylist(null));
	}
	
	@Test
	private void testRenamePlaylistWithValid() {
		assertEquals(StatusCode.SUCCESS, playlist.renamePlaylist("testPlaylist"));
	}
}
