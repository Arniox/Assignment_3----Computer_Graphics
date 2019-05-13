package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EventHandler implements KeyListener {
	//Main
	private Main main;
	
	//Constructor
	public EventHandler(Main main) {
		this.main = main;
		
		//Debug notification
		System.out.println("Press Comma (,): Toggle debugging. \t ||Default: "+(this.main.debugging? "on" : "off")+"\n"+
						   "Press Back Slash (\\): Toggle wire frame. ||Default: " +(this.main.wireFrame? "on" : "off")+ "\n"+
						   "Press Space: Toggle animation. \t\t ||Default:" +(this.main.animate? "on" : "off")+ "\n"+
						   "Press 1: Debugging Camera\n"+
						   "");
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
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
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
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.main.player.movingForward = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			this.main.player.movingBackwards = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.main.player.strafeLeft = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.main.player.strafeRight = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.main.player.movingForward = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			this.main.player.movingBackwards = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.main.player.strafeLeft = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.main.player.strafeRight = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	
}
