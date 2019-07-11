package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.awt.GLCanvas;

public class Debugging implements KeyListener {
	//Main
	private Main main;
	
	//Constructor
	public Debugging(GLCanvas canvas, Main main) {
		//Set main and canvas
		canvas.addKeyListener(this);	
		this.main = main;
	}
	
	//Debugg notification
	public void debugNotify() {
		//Debug notification
		System.out.println("\n\n--------------------------------------------------------------------------------------\n"+
						   "Press Comma (,): Toggle debugging. \t ||Default: "+(this.main.debugging? "on" : "off")+"\n"+
						   "Press Back Slash (\\): Toggle wire frame. ||Default: " +(this.main.wireFrame? "on" : "off")+ "\n"+
						   "Press Esc: Pause. \t\t\t ||Default: " +(!this.main.animate? "on" : "off")+ "\n"+
						   "Press 1: Debugging Camera. \t\t ||Default: "+(this.main.cameraInUse==0? "on" : "off")+"\n"+
						   "Press 2: First Person Camera. \t\t ||Default: "+(this.main.cameraInUse==1? "on" : "off")+"\n"+
						   "----------------------Current Time: "+(!this.main.nightOrDay? "Day" : "Night")+"--------------------------------\n"+
						   "Press W: Move Forwards\n"+
						   "Press S: Move Backwards\n"+
						   "Press A: Strafe Left\n"+
						   "Press D: Strafe Right\n"+
						   "Press Shift: Sprint\n"+
						   "Press L: Flash Light Toggle\n"+
						   "Move mouse left: Rotate Left\n"+
						   "Move mouse right: Rotate Right\n"+
						   "Move mouse up: Pitch up\n"+
						   "Move mouse down: Pitch down");
	}
	
	//Getters
	public Main getMain() {
		return this.main;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_COMMA) {
			this.main.debugging = !this.main.debugging;
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
			this.main.wireFrame = !this.main.wireFrame;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.main.animate = !this.main.animate;
		}
		if(e.getKeyCode() == KeyEvent.VK_1) {
			this.main.cameraInUse = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			this.main.cameraInUse = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			this.main.cameraInUse = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_L) {
			this.main.flashLightStatus = !this.main.flashLightStatus;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Main.player.sprint(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Main.player.sprint(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	
}
