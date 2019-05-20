package lightAndCamera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class FirstPersonCamera{
	
	//Camera hard limits
	private static final double MIN_FOV = 1;
	private static final double MAX_FOV = 90;
	
	//look at
	private double lookAt[] = {0,0,0};
	
	//Camer parameters
	private double fieldOfView = 60;
	private double windowWidth = 1;
	private double windowHeight = 1;
	
	//Glu
	private GLU glu = new GLU();
	
	//Constructor
	public FirstPersonCamera() {
		
	}
	
	//Draw
	public void draw(GL2 gl, float[] position, float heading, float pitch) {
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(fieldOfView, windowWidth/windowHeight, 0.1, 500);
		this.setLookAt(-heading, pitch, position);
		
		//Set up camera position and orientation
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(
				position[0], position[1]+1, position[2], 	//eye
				lookAt[0], lookAt[1], lookAt[2],			//center
				0, 1, 0);									//up
	}
	
	//Set look at
	public void setLookAt(float heading, float pitch, float[] pos) {		
		lookAt = new double[]{
				pos[0] + (float)Math.cos(Math.toRadians(heading)) * 2,
				pos[1] + (float)Math.sin(Math.toRadians(pitch)),
				pos[2] + (float)Math.sin(Math.toRadians(heading)) * 2
		};
	}
	
	//Set field of view
	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}
	
	//New Window size
	public void newWindowSize(int width, int height) {
		windowWidth = Math.max(1.0, width);
		windowHeight = Math.max(1.0, height);
	}

}
