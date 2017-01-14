package inputOutput;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.math.VectorUtil;

import main.Render;

public class CameraControl implements MouseMotionListener {
	private int lastX,lastY,currX,currY;
	private float pitch = 0.0f;
	private float yaw = -90.0f;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		lastX = currX;
		lastY = currY;
				
		currX = e.getX();
		currY = e.getY();
		if(currX > lastX){
			yaw+=0.1f;
		}
		else if(currX < lastX){
			yaw-=0.1f;
		}
		
		if(pitch < 89.0f && pitch >-89.0f){
			if(lastY > currY){
				pitch += 0.1f;
			}
			else if(lastY<currY){
				pitch -= 0.1f;
			}
		}
		else{
			pitch = (pitch>89.0f)?88.0f:-88.0f;
		}
		//System.out.println("LX:"+lastX+" LY:"+lastY+" CX:"+currX+" CY:"+currY);
	}

	public void updateRenderer(Render mainRender) {
		float[] gaze = new float[3];
		
		gaze[0] = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
		gaze[1] = (float) Math.sin(Math.toRadians(pitch));
		gaze[2] = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
		
		VectorUtil.normalizeVec3(gaze);
		System.out.println(pitch+"<<<pitch yaw>>>"+yaw);
		mainRender.gazeVector = gaze;
		
	}

}
