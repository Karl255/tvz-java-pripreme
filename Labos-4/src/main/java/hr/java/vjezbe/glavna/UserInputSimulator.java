package hr.java.vjezbe.glavna;

import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserInputSimulator implements Readable {
	private final String inputText;
	private int nextIndex = 0;

	public UserInputSimulator(String filePath) {
		try {
			this.inputText = Files.readString(Path.of(filePath));
		} catch (Exception e) {
			System.out.printf("Failed to open file: %s %n", filePath);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public int read(CharBuffer cb) {
		if (nextIndex < inputText.length()) {
			char nextChar = inputText.charAt(nextIndex++);
			printColored(nextChar);
			cb.put(nextChar);
			return 1;
		} else {
			return -1;
		}
	}
	
	public void printColored(char c) {
		System.out.printf("\u001b[38;2;0;127;0m%c\u001b[0m", c);
	}
}
