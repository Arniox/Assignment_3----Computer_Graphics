package lightAndCamera;

import com.jogamp.opengl.GL2;

public class FlashLight {
	//Main variables
	private GL2 gl;
	
	//Flashlight variables
	private float[] starterPos = new float[]{0,0,0,0};
	private float[] starterDir = new float[]{1,-1,0};
	private float theta = 25;
	
	//constructor
	public FlashLight(GL2 gl, float[] starterPos, float xStarterHeading, float yStarterHeading) {
		//Set main
		this.gl = gl;
		
		//Set up starter positions
		this.starterPos = starterPos;
		//this.starterDir = starterDir;
	}
	
	//Run on display
	public void runOnDisplay(boolean flashLightStatus) {
		gl.glShadeModel(GL2.GL_SMOOTH);
		if(flashLightStatus) {
			//Draw flashlight
			gl.glEnable(GL2.GL_LIGHT2);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, new float[]{0,0,0,1}, 0);
			gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, new float[]{1,-1,0}, 0);
		}else {
			gl.glDisable(GL2.GL_LIGHT2);
		}
	}
	
	//Run on initiation
	public void initFlashLight() {
		//Diffuse, secular and ambient
		float diffuseFlashLight[] = {1,1,1,1};
		float specularFlashLight[] = {1,1,1,1};
		float ambientFlashLight[] = {1,1,1,1};
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, diffuseFlashLight, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, specularFlashLight, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, ambientFlashLight, 0);
		//Starting position and set up of flashLight
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, starterPos, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, starterDir, 0);
		//Cutoff angle
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, theta);
	}
	
	//Close
	public void close() {
		this.gl = null;
		this.starterDir = null;
		this.starterPos = null;
		System.out.println("FlashLight closed...");
	}
}
