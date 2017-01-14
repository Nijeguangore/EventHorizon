package inputOutput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.math.VectorUtil;

import main.Render;

public class KeyStroke implements KeyListener {

	private boolean continueStroke = true;
	private boolean[] keysDown = new boolean[1024];
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyDown = e.getKeyCode();
		continueStroke = !(keyDown == e.VK_ESCAPE);
		if(continueStroke){
			keysDown[keyDown] = true;
		}
		

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyDown = e.getKeyCode();

		if(continueStroke){
			keysDown[keyDown] = false;
		}
	}

	public boolean continueRun() {
		
		return continueStroke;
	}

	public void updateCamera(Render mainRender) {
		float[] directionVector = mainRender.gazeVector;
		float[] rightVector = new float[3];

		VectorUtil.scaleVec3(directionVector, directionVector, 0.01f);

		VectorUtil.crossVec3(rightVector, 0, mainRender.Up , 0, mainRender.gazeVector, 0);

		
		if(keysDown[KeyEvent.VK_W]){
			VectorUtil.addVec3(mainRender.eyeLocation, directionVector , mainRender.eyeLocation);
		}
		else if(keysDown[KeyEvent.VK_S]){
			VectorUtil.subVec3(mainRender.eyeLocation, mainRender.eyeLocation, directionVector);
		}
		
		if(keysDown[KeyEvent.VK_D]){
			VectorUtil.subVec3(mainRender.eyeLocation, mainRender.eyeLocation,rightVector);
		}
		else if(keysDown[KeyEvent.VK_A]){
			VectorUtil.addVec3(mainRender.eyeLocation, rightVector, mainRender.eyeLocation);
		}
	}

}
