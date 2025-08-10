import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyAdapter;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.NativeInputEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		try {
			File file = new File("API_KEY");
			if (!file.exists()) {
				String apiKey = JOptionPane.showInputDialog(null, "Enter API_KEY:");
				FileWriter writer = new FileWriter(file);
				writer.write(apiKey);
				writer.close();
			}
		} catch (IOException exc) {
			return;
		}

		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new Yacayogo());
		} catch (NativeHookException exc) {
			exc.printStackTrace();
		}

		System.out.println("\nYacayoga: (adj.) ready to comply with another's request.\n");
	}
}

class Yacayogo extends NativeKeyAdapter{
	boolean running = false;

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (this.running) {
			return;
		}
		int modifiers = e.getModifiers();
		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
		String modifierText = NativeInputEvent.getModifiersText(modifiers);
		if (modifierText.equals("Shift+Ctrl+Alt") && keyText == "Y") {
			Typer.copy();
			String clipText = ClipboardReader.getClipboardText();

			if (clipText == null) return;

			this.running = true;
			final Typer typer = new Typer();
			System.out.println("Query: " + clipText);
			QueryAI.asyncRequest(clipText, (response) -> {
				System.out.println("Response found!\n");
				typer.setResponse(response);
			});
			if (!typer.process()) {
				System.out.println("Timeout!");
				typer.backspace(19);
				typer.idk();
			};
			this.running = false;
		}
	}
}
