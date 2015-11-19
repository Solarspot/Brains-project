package Solar.BF;

import java.util.ArrayList;

/**
 * This represents the control flow graph of a program. Top level loops are
 * represented in topLevelLoops, each of these may have n children. The program
 * may be only partially scanned, in which case scannedTo != programSize.
 */
public class ControlFlowPartial {
	private int scannedTo;
	private int programSize;
	private StringBuffer programText;
	private Block currentBlock;
	private Block root;

	public ControlFlowPartial(StringBuffer programText) {
		scannedTo = 0;
		this.programSize = programText.length();
		this.programText = programText;
		// Using a dummy block to contain the entire program.
		root = new Block();
		/*
		 * Overrides null with something non-null. root has a flag to check to
		 * avoid following this.
		 */
		root.parent = root;
		currentBlock = root; // So we have somewhere to start.
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
		boolean root; // This should never change post-construction.

		/**
		 * Constructor for a single control flow block.
		 * 
		 * @param beginning
		 *            Index of opening '[' of some loop, this constructor
		 *            assumes the corresponding ']' has not been spotted.
		 * @param parent
		 *            The parent Block of this one.
		 */
		public Block(int beginning, Block parent) {
			this.beginning = beginning;
			end = -1; // Sentinel value, must be non-negative before use.
			this.parent = parent;
			children = new ArrayList<Block>();
			root = false;
		}

		/**
		 * Constructor for zero-value of Block. Used to represent root of tree.
		 */
		public Block() {
			beginning = 0;
			end = programSize - 1;
			parent = null;
			children = new ArrayList<Block>();
			root = true;
		}
	}

	public void foundBlock(int programCounter) {
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

			// We've now scanned up to this character.
			scannedTo = programCounter;
		}

		/*
		 * Moving programCounter into the block if cell != 0, or skipping it if
		 * cell == 0, is not our responsibility.
		 */

	}

	/**
	 * Skips this block.
	 * 
	 * @param programCounter
	 *            the index of the block to skip.
	 */
	public int skipBlock(int programCounter) {

		return 0;
	}
}
