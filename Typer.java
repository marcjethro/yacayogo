import java.awt.AWTException;
import java.awt.Robot;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_C;

public class Typer {
	Robot robot;
	Keyboard keyboard;
	boolean processing = false;

	Typer() {
		try {
			this.robot = new Robot();
			this.keyboard = new Keyboard(robot);
		 } catch (AWTException e) {
			e.printStackTrace();
		 }
	}

	public void respond(String string) {
		while (this.processing) {
			robot.delay(100);
		}
		for (int i = 0; i < 13; i++) {
			keyboard.doType(VK_BACK_SPACE);
			robot.delay(25);
		}
		for (char c : string.toCharArray()) {
			keyboard.type(c);
			robot.delay(10 + (int) (Math.random()*20));
		}
	}

	public void process() {
		this.processing = true;
		keyboard.doType(VK_RIGHT);
		for (char c : "\n\nprocessing".toCharArray()) {
			keyboard.type(c);
			robot.delay(100);
		}
		robot.delay(1000);
		for (char c : "...".toCharArray()) {
			keyboard.type(c);
			robot.delay(1000);
		}
		this.processing = false;
	}

	public void copy() {
		robot.delay(500);
		keyboard.doType(VK_CONTROL, VK_C);
		robot.delay(500);
	}
}
