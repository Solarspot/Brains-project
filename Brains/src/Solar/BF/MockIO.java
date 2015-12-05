package Solar.BF;

import java.io.IOException;

public interface MockIO {
	public byte read() throws IOException;

	public void print(byte memory);
}
