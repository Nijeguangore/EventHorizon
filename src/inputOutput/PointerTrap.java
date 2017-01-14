package inputOutput;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseListener;

import com.jogamp.newt.event.MouseEvent;

public class PointerTrap implements MouseListener {
	private int Xcenter = 0;
	private int Ycenter = 0;
	
	public PointerTrap(int x, int y) {
		Xcenter = x/2;
		Ycenter = y/2;
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		Robot cursorReset;
		try {
			cursorReset = new Robot();
			cursorReset.mouseMove(Xcenter, Ycenter);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
