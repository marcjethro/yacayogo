import java.awt.AWTException;
import java.awt.Robot;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_C;

public class Typer {
	Robot robot;
	Keyboard keyboard;
	String response = null;

	Typer() {
		try {
			this.robot = new Robot();
			this.keyboard = new Keyboard(robot);
		 } catch (AWTException e) {
			e.printStackTrace();
		 }
	}

	public void setResponse(String response) {
		this.response = response;
	}

	private void respond() {
		for (char c : this.response.toCharArray()) {
			keyboard.type(c);
			robot.delay(10 + (int) (Math.random()*20));
		}
	}

	public void backspace(int num) {
		for (int i = 0; i < num; i++) {
			keyboard.doType(VK_BACK_SPACE);
			robot.delay(25);
		}
	}

	public void idk() {
		for (char c : "Timeout! Please try again.".toCharArray()) {
			keyboard.type(c);
			robot.delay(10 + (int) (Math.random()*20));
		}
	}

	public boolean process() {
		keyboard.doType(VK_RIGHT);
		for (char c : "\n\nprocessing".toCharArray()) {
			keyboard.type(c);
			robot.delay(100);
		}

		int backspaces = "processing".length();
		robot.delay(1000);
		for (int i=0; i<5; i++) {
			if (this.response != null) {
				this.backspace(backspaces);
				this.respond();
				return true;
			}
			keyboard.type('.');
			backspaces += 1;
			robot.delay(1000);
		}
		return false;
	}

	public static void copy() {
		try {
			Robot copyRobot = new Robot();
			Keyboard copyKeyboard = new Keyboard(copyRobot);
			copyRobot.delay(500);
			copyKeyboard.doType(VK_CONTROL, VK_C);
			copyRobot.delay(500);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
