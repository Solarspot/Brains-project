package Solar.BF;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FuckedBrainInterface {

	public static void main(String[] args) throws IOException {
		// TODO User-interface, maybe a BF IDE someday? For now...
		// Either a
		JFileChooser fileChooser = new JFileChooser();
		JFrame window = new JFrame("Pick a BF program");

		int result = fileChooser.showOpenDialog(window);
		window.dispose();
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Starting program.");
			run(selectedFile);
		}
	}

	/**
	 * Runs the selected BF program
	 * 
	 * @param program
	 *            selected
	 * @throws IOException
	 */
	static void run(File program) throws IOException {
		String code = "";
		code = new String(Files.readAllBytes(program.toPath()));
		BrainfuckSimple interpreter = new BrainfuckSimple(code);
		System.out.println(code);
		interpreter.run();

	}
}
