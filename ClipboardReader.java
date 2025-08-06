import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardReader {
	public static String getClipboardText() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			// Check if the clipboard contains text data
			if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
				return (String) clipboard.getData(DataFlavor.stringFlavor);
			}
		} catch (UnsupportedFlavorException e) {
			System.err.println("Unsupported data flavor on clipboard: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error accessing clipboard data: " + e.getMessage());
		}
		return null; // Return null if no text is found or an error occurs
	}
}
