package Solar.BF;

import java.io.IOException;
import java.util.ArrayList;
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
	private LinkedList<Byte> tape; // Working data of program
	private int stackPointer;
	private ControlFlowPartial graph; // Root of tree of blocks.
	private int furthestScanned;

	// First bracket on spotting it, to speed up returning to the start of
	// loops.

	/**
	 * Start empty interpreter.
	 */
	public BrainfuckLight() {
		programText = new StringBuffer(50);
		programSize = 0;
		programPointer = 0;
		stackPointer = 0;
		furthestScanned = 0;
		world = new InputOutput();
		tape = new LinkedList<Byte>();
		graph = new ControlFlowPartial(programText); // Mark block beginnings
														// and endings to
		// speed up
		// traversals
	}

	/**
	 * Start an interpreter on program source.
	 */
	public BrainfuckLight(String programText) {
		this.programText = new StringBuffer(programText);
		programSize = programText.length();
		programPointer = 0;
		stackPointer = 0;
		furthestScanned = 0;
		world = new InputOutput();
		tape = new LinkedList<Byte>();
		graph = new ControlFlowPartial(this.programText); // Mark block
															// beginnings and
															// endings to
		// speed up traversals
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
				cell = tape.get(stackPointer);

				switch (programText.charAt(programPointer)) {

				case '+': // Increment operator
					tape.set(stackPointer, (byte) (cell + 1));
					break;

				case '-': // Decrement operator
					tape.set(stackPointer, (byte) (cell - 1));
					break;

				case '>': // Increment stackPointer
					tape.set(stackPointer, (byte) (cell));
					stackPointer++;
					// Expand stack, if program has indexed off existing stack:
					if (tape.size() < stackPointer) {
						tape.push((byte) 0);
					}
					break;

				case '<': // Decrement stackPointer
					tape.set(stackPointer, (byte) (cell));
					stackPointer--;
					if (stackPointer < 0) {
						System.err
								.println("Error!  Gone off the deep end!  Negative memory address.");
					}
					break;

				case '[': // Loop start
					openBracket(cell);
					break;

				case ']': // Loop end
					if (cell == 0) {

					}
					break;

				case ',': // Input
					tape.set(stackPointer, world.read());
					break;

				case '.': // Output
					world.print((byte) cell);
					break;
				}

				programPointer++;
			}

	}

	/**
	 * Implement loop beginnings.
	 * 
	 * @return
	 */
	private void openBracket(int cell) {
		// Haven't seen this one yet, add new block
		if (programPointer > furthestScanned) {
			graph.openBracket(programPointer, cell);
		} else if (cell == 0) {
			// Goto corresponding ']' and possibly scan further for new blocks.
		}
		// else enter the block, with the next instruction. run() does this
		// already.
	}

	/**
	 * Scan this block, and add it and child blocks to the parent block.
	 */

	public static class UnblancedLoopException extends java.lang.Exception {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Unit of control flow, including child Blocks.
	 */
	private static class Block {
		int beginning = -1;
		int end = -1;
		ArrayList<Block> children = new ArrayList<Block>();
	}
}
