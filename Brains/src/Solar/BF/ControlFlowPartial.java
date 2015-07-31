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
	private StringBuffer programText;
	private Block currentBlock;

	public ControlFlowPartial(StringBuffer programText) {
		topLevelLoops = new ArrayList<Block>();
		scannedTo = 0;
		this.programSize = programText.length();
		this.programText = programText;
	}

	/**
	 * Unit of control flow, including child Blocks. parent == null is used to
	 * represent top-level loops
	 */
	public class Block {
		int beginning;
		int end;
		Block parent;
		int index;
		ArrayList<Block> children;

		/**
		 * Constructor for a single control flow block.
		 * 
		 * @param beginning
		 *            Index of opening '[' of some loop, this constructor
		 *            assumes the corresponding ']' has not been spotted.
		 * @param parent
		 *            The parent Block of this one. Set to null for a top-level
		 *            block.
		 */
		public Block(int beginning, Block parent) {
			this.beginning = beginning;
			end = -1; // Sentinel value, must be non-negative before use.
			this.parent = parent;
			children = new ArrayList<Block>();

		}
	}

	public int openBracket(int programCounter, int cell) {
		// We're at least this far, if we're being called with this.
		scannedTo = programCounter;

		/*
		 * We need to have the block ready, whether we're entering it or
		 * skipping it.
		 */
		if (programCounter > scannedTo) {
			Block parent = currentBlock;
			/*
			 * We need a new Block to represent this one. This is the beginning
			 * of a block, so this one must be contained inside this.current
			 */
			currentBlock = new Block(programCounter, parent);
			parent.children.add(currentBlock);
			currentBlock.index = parent.children.size();
		}
		if (cell == 0) {
			// We must goto the corresponding ']' character.
		} else if (currentBlock.beginning != programCounter
				&& currentBlock.parent != null) {
			// We're entering the block, set it to current.
			ArrayList<Block> others = currentBlock.parent.children;
			/*
			 * We may assume that if we're not going up a level, not at top
			 * level, the next block has already been scanned and added to
			 * parent.children
			 */
			currentBlock = others.get(currentBlock.index + 1);
		} else if (currentBlock.parent == null
				&& currentBlock.beginning != programCounter) {
			// Same as above, but special-cased for top level loops.
			int i = currentBlock.index;

		}
		return programCounter;

		// Moving programCounter into the block is not our responsibility

	}
}
