package Solar.BF;

import java.util.ArrayList;
import java.util.LinkedList;

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
	private LinkedList<Integer> indexStack;

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
		// The last block's index seen in currentBlock.
		indexStack = new LinkedList<Integer>();
		indexStack.push(-1); // Dummy, will be zero on seeing child block at 0.
	}

	/**
	 * Unit of control flow, including child Blocks. parent == null is used to
	 * represent top-level loops
	 */
	public class Block {
		int beginning;
		int end;
		Block parent;
		int indexInParent;
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
			currentBlock.indexInParent = parent.children.size();

			// We've now scanned up to this character.
			scannedTo = programCounter;
		} else {
			int childIndex = 0;
			Block parent = currentBlock;
			Block child;
			/*
			 * programCounter is already pointing at this block, set it as
			 * current. Also, skipBlock assumes it's skipping the currentBlock.
			 */
			childIndex = indexStack.pop();
			childIndex++;
			indexStack.push(childIndex);
			child = parent.children.get(childIndex);
			// Sanity check: Only way we should ever get here, to an already
			// Found block, is if beginning is the start of the block following
			// last.
			if (child.beginning == programCounter) {
				currentBlock = child;
			} else {
				// World is burning, all hope is lost, we have a bug.
				throw new RuntimeException();
			}
		}
		/*
		 * Do we really want to set a block to current / add it to indexStack if
		 * programCounter isn't in it yet, and we'll need indexStack's previous
		 * Top to leave this...
		 */
		indexStack.push(-1);
		/*
		 * Moving programCounter into the block if cell != 0, or skipping it if
		 * cell == 0, is not our responsibility.
		 */

	}

	/**
	 * Skips this block. Assumes the block is also currentBlock
	 * 
	 * @param programCounter
	 *            the source index of the block to skip.
	 */
	public int skipBlock(int programCounter) {

		if (currentBlock.beginning == programCounter) {
			// Block must be scanned before exiting.
			if (currentBlock.end == -1) {
				// TODO scan block's contents.
			}
			// Exit block.
			return leaveBlock();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * Leaves the current block. Returns programCounter after leaving.
	 */
	public int leaveBlock() {
		Block parent = currentBlock.parent;
		int end = currentBlock.end;
		currentBlock = parent;
		indexStack.pop();
		return end;
	}

	/**
	 * Repeats current block. Returns beginning of block's programCounter.
	 */
	public int repeatBlock() {
		indexStack.pop(); // Repeating implies redoing all child blocks.
		indexStack.push(-1);
		return currentBlock.beginning;
	}
}
