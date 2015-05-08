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
	StringBuffer programText; // Brainfuck source code
	private int programPointer;
	private int programSize;
	private InputOutput world;
	LinkedList<Byte> programStack; // Working data of program
	private int stackPointer;
	LinkedList<Integer> loopStarts; // [ text ], mark the

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
		loopStarts = new LinkedList<Integer>(); // [ text ], mark the
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
		loopStarts = new LinkedList<Integer>(); // [ text ], mark the
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
		int i = 0; // Temp variable

		while (programPointer < programSize)
			herp: {
				switch (programText.charAt(programPointer)) {

				case '+': // Increment operator
					i = programStack.get(stackPointer);
					programStack.set(stackPointer, (byte) (i + 1));
					break;

				case '-': // Decrement operator
					i = programStack.get(stackPointer);
					programStack.set(stackPointer, (byte) (i - 1));
					break;

				case '>': // Increment stackPointer
					stackPointer++;
					// Expand stack, if program has indexed off existing stack:
					i = programStack.size();
					if (i < stackPointer) {
						programStack.push((byte) 0);
					}
					break;

				case '<': // Decrement stackPointer
					stackPointer--;

					break;

				case '[': // Loop start
					/*
					 * If the value under stackPointer is non-zero, push
					 * programPointer to loopStarts, and continue execution. If
					 * zero, skip execution of all commands until the
					 * corresponding ']' is found; resume execution there.
					 */

					i = programStack.get(stackPointer);
					if (i != 0) {
						loopStarts.push(programPointer);
					} else {
						i = programPointer;
						while (programText.charAt(i) != ']') {
							i++;
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
					if (programStack.get(stackPointer) == 0) {

					}
					break;

				case ',': // Input
					world.read();
					break;

				case '.': // Output
					world.print(programStack.get(stackPointer));
					break;
				}

				programPointer++;
			}

	}

	public static class UnblancedLoopException extends java.lang.Exception {
		private static final long serialVersionUID = 1L;
	}
}
