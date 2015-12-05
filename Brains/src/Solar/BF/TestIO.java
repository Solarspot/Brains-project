package Solar.BF;

import java.io.IOException;
import java.util.ArrayList;

public class TestIO implements MockIO {

	private byte[] testCase;
	private byte[] expectedOutput;
	ArrayList<Byte> actualOutput;
	private int inputIndex = -1;
	private int outputIndex = -1;
	int outputDeviations = 0;

	public TestIO(byte[] testCase, byte[] expectedOutput) {
		this.testCase = testCase;
		this.expectedOutput = expectedOutput;
		actualOutput = new ArrayList<Byte>(expectedOutput.length);
	}

	@Override
	public byte read() throws IOException {
		inputIndex++;
		if (inputIndex < testCase.length) {
			return testCase[inputIndex];
		}
		return 0; // BF programs ideally will terminate on EOF. It will see this
					// for the rest of time if it keeps trying. Could throw an
					// exception.
	}

	@Override
	public void print(byte memory) {
		outputIndex++;
		actualOutput.add(memory);
		if (outputIndex < expectedOutput.length) {
			if (expectedOutput[outputIndex] != actualOutput.get(outputIndex)) {
				outputDeviations++;
			}
		} else {
			outputDeviations++;
		}
	}

}
