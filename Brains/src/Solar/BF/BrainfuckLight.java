package Solar.BF;

import java.io.IOException;
import java.util.LinkedList;

/**
 * This is a brainfuck interpreter, optimised for minimal startup time. It may
 * be initialised with source code and existing memory, source code and no
 * memory, or memory but no source code. Once an interpreter is available, it
 * may be called with new program code (and existing memory) or new code may be
 * added to whatever it currently has.
 */
public class BrainfuckLight {
	private StringBuffer programText; // Brainfuck source code
	private int programPointer;
	private int programSize;
	private InputOutput world;
	private LinkedList<Byte> programStack; // Working data of program
	private int stackPointer;
	private LinkedList<Block> loops; // [ text ], mark the

	// First bracket on spotting it, to speed up returning to the start of
	// loops.

	/**
	 * Start empty interpreter.
	 */
	public BrainfuckLight() {
		programText = new StringBuffer(50);
		programSize = 0;
		world = new InputOutput();
		programStack = new LinkedList<Byte>();
		loops = new LinkedList<Block>(); // [ text ], mark the
		// First bracket on spotting it, to speed up returning to the start of a
		// loop.
	}

	/**
	 * Start an interpreter on program source.
	 */
	public BrainfuckLight(String programText) {
		this.programText = new StringBuffer(programText);
		programSize = programText.length();
		world = new InputOutput();
		programStack = new LinkedList<Byte>();
		loops = new LinkedList<Block>(); // [ text ], mark the
		// First bracket on spotting it, to speed up returning to the start of a
		// loop.
	}

	// TODO: Constructors for interpreter memory and combined text-memory

	/**
	 * Run the program.
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		int cell = 0; // Current memory

		while (programPointer < programSize)
			herp: {
				cell = programStack.get(stackPointer);

				switch (programText.charAt(programPointer)) {

				case '+': // Increment operator
					programStack.set(stackPointer, (byte) (cell + 1));
					break;

				case '-': // Decrement operator
					programStack.set(stackPointer, (byte) (cell - 1));
					break;

				case '>': // Increment stackPointer
					stackPointer++;
					// Expand stack, if program has indexed off existing stack:
					if (programStack.size() < stackPointer) {
						programStack.push((byte) 0);
					}
					break;

				case '<': // Decrement stackPointer
					stackPointer--;
					if (stackPointer < 0) {
						System.err
								.println("Error!  Gone off the deep end!  Negative memory address.");
					}
					break;

				case '[': // Loop start
					/*
					 * If the value under stackPointer is non-zero, push
					 * programPointer to loopStarts, and continue execution. If
					 * zero, skip execution of all commands until the
					 * corresponding ']' is found; resume execution there.
					 */

					if (cell != 0) {

					} else {
						cell = programPointer;
						while (programText.charAt(cell) != ']') {
							cell++;
							/*
							 * if (i >= programSize) { System.out.println(
							 * "Unbalanced brackets; ending execution.");
							 * ErrorLog.addError(
							 * "BF-l encountered unbalanced loop brackets.");
							 * break herp; }
							 */
						}
					}
					break;

				case ']': // Loop end
					if (cell == 0) {

					}
					break;

				case ',': // Input
					programStack.set(stackPointer, world.read());
					break;

				case '.': // Output
					world.print(programStack.get(stackPointer));
					break;
				}

				programPointer++;
			}

	}

	/**
	 * Find the corresponding closing bracket
	 * 
	 * @return
	 */
	private void openBracket() {

	}

	public static class UnblancedLoopException extends java.lang.Exception {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Markers for block delimiters. -1 is used as an impossible sentinel value.
	 *
	 */
	private class Block {
		int beginning = -1;
		int end = -1;
	}
}
