package Solar.BF;

import java.util.ArrayList;

/**
 * This represents the control flow graph of a program. Top level loops are
 * represented in topLevelLoops, each of these may have n children. The program
 * may be only partially scanned, in which case scannedTo != programSize.
 */
public class ControlFlowPartial {
	private ArrayList<Block> topLevelLoops;
	private int scannedTo;
	private int programSize;

	public ControlFlowPartial(int programSize) {
		topLevelLoops = new ArrayList<Block>();
		scannedTo = 0;
		this.programSize = programSize;
	}

	/**
	 * Unit of control flow, including child Blocks.
	 */
	private static class Block {
		int beginning = -1;
		int end = -1;
		Block parent;
		ArrayList<Block> children = new ArrayList<Block>();
	}
}
