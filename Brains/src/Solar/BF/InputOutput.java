package Solar.BF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputOutput {
	private StringBuffer input;
	public BufferedReader read;

	public InputOutput() {
		read = new BufferedReader(new InputStreamReader(System.in));
		input = new StringBuffer();
		// Input will be built up and flushed by calls to . and ,
	}

	/**
	 * Reads n (byte)characters from stdin, returns 1 for each call. Further
	 * calls not separated by prints will read from further in the buffer.
	 * 
	 * @throws IOException
	 */
	public byte read() throws IOException {
		int buffered = input.length();

		// Infinite loop, if input stays empty.
		while (buffered == 0) {
			input.append(read.readLine());
			buffered = input.length();
		}
		return (byte) (input.codePointAt(0));
	}

	/**
	 * Prints one (char)byte, and clears input.
	 * 
	 * @param memory
	 *            Data to print.
	 * 
	 */
	public void print(byte memory) {
		System.out.print((char) memory);
		input.delete(0, input.length() - 1);
	}
}