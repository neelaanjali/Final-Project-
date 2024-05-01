package accountsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.UserAccount;
import playlists.Playlist;

public class UserAccountTest {
	
	private UserAccount account = null;
	
	@BeforeEach
	void setUp() {
		account = new UserAccount("temp", "password", new ArrayList<Playlist>());
	}
	
	@Test
	void testGetUsername() {
		assertEquals("temp", account.getUsername());
	}
	
	@Test
	void testSetUsername() {
		account.setUsername("newname");
		assertEquals("newname", account.getUsername());
	}
	
	@Test
	void testGetPassword() {
		assertEquals("password", account.getPassword());
	}
	
	@Test
	void testSetPassword() {
		account.setPassword("newpassword");
		assertEquals("newpassword", account.getPassword());
	}
	
	@Test
	void testSetGetPlaylists() {
		ArrayList<Playlist> playlists = new ArrayList<Playlist>();
		account.setPlaylists(playlists);
		assertEquals(playlists, account.getPlaylists());
	}
}
