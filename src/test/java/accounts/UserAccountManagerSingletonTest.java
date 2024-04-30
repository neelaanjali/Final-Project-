package accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import program.StatusCode;

public class UserAccountManagerSingletonTest {
	private UserAccountManagerSingleton manager;
	
	@BeforeEach
	public void setUp() {
		manager = UserAccountManagerSingleton.getInstance();
	}
	
	@Test
	public void testLoginWithValidCreditials() {
		assertEquals(StatusCode.SUCCESS, manager.login("test", "Test1234"));
	}
	
	@Test
	public void testLoginWithNullValues() {
		assertEquals(StatusCode.INVALID_INPUT, manager.login(null, null));
	}
	
	@Test
	public void testLoginWithInvalidCredentials() {
		assertEquals(StatusCode.NOT_FOUND, manager.login("fakeusername", "fakepassword"));
	}
	
	@Test
	public void testRegisterWithNullValues() {
		assertEquals(StatusCode.INVALID_INPUT, manager.register(null, null));
	}
	
	@Test
	public void testRegisterWithTakenUsername() {
		assertEquals(StatusCode.INVALID_INPUT, manager.register("test", "password"));
	}
	
	@Test
	public void testRegisterWithInvalidPassword() {
		assertEquals(StatusCode.INVALID_INPUT, manager.register("fakeusername", "123"));
	}
	
	@Test
	public void testWriteToFile() {
		assertEquals(StatusCode.SUCCESS, manager.writeToFile("testFile.csv"));
	}
	
	@Test
	public void testReadFromFile() {
		assertEquals(StatusCode.SUCCESS, manager.readFromFile("testFile.csv"));
	}
}
