package Solar.BF;

import java.io.IOException;

public class BrainfuckSimple {

	private String programText;
	private InputOutput world;
	private byte[] memory;
	private int address;
	private int command;

	public BrainfuckSimple(String programText) {
		this.programText = programText; // BF source code.
		world = new InputOutput();
		// Minimum allowable memory size is 30k cells of 8 bits.
		memory = new byte[30000];
		// Programs start with nothing. Nothing I tell you.
		command = 0;
		address = 0;
	}

	/**
	 * Simple Brainfuck interpreter.
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		char instruction = ' ';

		// Program returns when execution runs off the end.
		while (command < programText.length()) {
			instruction = programText.charAt(command);

			switch (instruction) {
			case '+': // BF increment command
				memory[address]++;
				command++;
				break;
			case '-': // Decrement command
				memory[address]--;
				command++;
				break;
			case '>': // Either of the change-address instructions could move
						// beyond bounds of memory.
				address++;
				command++;
				break;
			case '<': // Pointer movement commands
				address--;
				command++;
				break;
			case '.': // Print command
				world.print(memory[address]);
				command++;
				break;
			case ',': // Input command
				memory[address] = world.read();
				command++;
				break;
			case '[': // Brackets delimit blocks
				openBracket();
				break;
			case ']':
				closeBracket();
				break;
			// All other characters do nothing.
			}
		}
	}

	/**
	 * Handles reaching the end of a block. Finds beginning of block (passing by
	 * contained blocks). If loop will not repeat, we can skip ahead.
	 */
	private void closeBracket() {
		int indentationLevels = 0;
		boolean found = false;
		char instruction = programText.charAt(command);

		if (memory[address] == 0) {
			// We can skip finding the corresponding
			// open bracket, and just move right along...
			command++;
		} else {
			// Find corresponding open bracket.
			do {
				command--;
				instruction = programText.charAt(command);
				if (instruction == ']') {
					indentationLevels++;
				} else if (instruction == '[') {
					if (indentationLevels > 0) {
						indentationLevels--;
					} else {
						found = true;
						command++; // Instruction after beginning of block.
					}
				}
			} while (found == false && command > 0);
		}
	}

	private void openBracket() {
		int indentationLevels = 0;
		boolean found = false;
		char instruction = programText.charAt(command);

		if (memory[command] != 0) {
			// Enter block
			command++;
		} else {
			do {
				command++;
				instruction = programText.charAt(command);
				if (instruction == '[') {
					indentationLevels++;
				} else if (instruction == ']') {
					if (indentationLevels > 0) {
						indentationLevels--;
					} else {
						found = true;
						command++; // Move past the block
					}
				}
			} while (found == false && command <= programText.length());
		}
	}
}
