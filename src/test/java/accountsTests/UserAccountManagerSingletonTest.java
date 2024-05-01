package accountsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.UserAccountManagerSingleton;
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
		assertEquals(StatusCode.INVALID_INPUT, manager.register("", null));
	}
	
	@Test
	public void testRegisterWithTakenUsername() {
		assertEquals(StatusCode.INVALID_INPUT, manager.register("test", "password"));
	}
	
	@Test
	public void testRegisterWithInvalidPassword() {
		assertEquals(StatusCode.INVALID_INPUT, manager.register("thisusernamedoesnotexist", "123"));
	}
	
	@Test
	public void testRegisterWithValidCredentials() throws IOException {
		StatusCode result = null;
		// save the contents of the file
		// this ensures the user accounts file isn't modified while testings
		BufferedReader br = new BufferedReader(new FileReader(UserAccountManagerSingleton.getUseraccountsfile()));
		ArrayList<String> fileContents = new ArrayList<String>();

		String line = br.readLine();
		while (line != null && !line.isEmpty() && !line.isBlank()) {
			fileContents.add(line);
			line = br.readLine();
		}

		br.close();

		result = manager.register("fakeusername", "password");

		// load the contents back into the file
		BufferedWriter bw = new BufferedWriter(new FileWriter(UserAccountManagerSingleton.getUseraccountsfile()));
		for (String fileLine : fileContents) {
			bw.write(fileLine);
		}

		bw.close();

		assertEquals(StatusCode.SUCCESS, result);

	}
	
	@Test
	public void testWriteToFile() {
		assertEquals(StatusCode.SUCCESS, manager.writeToFile("testFile.csv"));
	}
	
	@Test
	public void testReadFromFile() {
		assertEquals(StatusCode.SUCCESS, manager.readFromFile("testFile.csv"));
	}
	
	@Test
	public void testReadFromFileWithNull() {
		assertEquals(StatusCode.EXCEPTION, manager.readFromFile(null));
	}
	
	@Test
	public void testReadFromFileWithWrongPath() {
		assertEquals(StatusCode.NOT_FOUND, manager.readFromFile("this/file/does/not/exist!"));
	}
	
	@Test
	public void testWriteToFileWithNull() {
		assertEquals(StatusCode.EXCEPTION, manager.writeToFile(null));
	}
}
