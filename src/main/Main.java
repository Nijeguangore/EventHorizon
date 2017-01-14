package main;

import java.awt.event.MouseListener;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLJPanel;

import inputOutput.PointerTrap;
import inputOutput.CameraControl;
import inputOutput.KeyStroke;

public class Main {

	public static void main(String[] args) {
		
		JFrame mainFrame = new JFrame("Les Go!");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1200, 900);
		
		GLJPanel context = new GLJPanel();
		Render mainRender = new Render();
		context.addGLEventListener(mainRender);
		
		
		mainFrame.getContentPane().add(context);
		mainFrame.setVisible(true);
		
		KeyStroke keyListener = new KeyStroke();
		context.addKeyListener(keyListener);
		
		PointerTrap pointerCapture = new PointerTrap(1200,900);
		context.addMouseListener(pointerCapture);
		
		CameraControl renderCam = new CameraControl(); 
		context.addMouseMotionListener(renderCam);
		
		while(keyListener.continueRun()){
			renderCam.updateRenderer(mainRender);
			keyListener.updateCamera(mainRender);
			context.display();
		}
		
		mainFrame.dispose();
	}

}
