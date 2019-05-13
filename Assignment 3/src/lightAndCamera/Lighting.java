package lightAndCamera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Lighting {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Sun and moon pos
	private float[] sunPos;
	private float[] moonPos;
	
	//Enable flashlight
	private boolean flashLight = false;
	private float[] flashLightPos = new float[]{0,70,0,0};
	private float[] flashLightDirection = new float[]{0,-1,0};
	private float theta = 20;
	
	//Consturctor
	public Lighting(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}	
	
	public void useLighting() {
		//Lighting stuff
		//Global ambient light
		float globalAmp[] = {0.4f, 0, 0, 1};
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmp, 0);
		
		//The sun
		float diffuseSun[] = {1, 1, 1, 1 };
		float specularSun[] = { 1, 1, 1, 1 };
		float ambientSun[] = { 1, 1, 1, 1 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientSun, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseSun, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularSun, 0);
		//Position of the sun
		sunPos = new float[]{0,100,0,1};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, sunPos, 0);
		
		//The moon
		float diffuseMoon[] = {0.25f, 0.25f, 1, 1};
		float specularMoon[] = {0.25f, 0.25f, 1, 1};
		float ambientMoon[] = {0.25f, 0.25f, 1, 1};
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambientMoon, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuseMoon, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specularMoon, 0);
		//Position of the moon
		moonPos = new float[]{0,100,0,1};
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, moonPos, 0);
		
		//FlashLight
		float diffuseFlashLight[] = {1,1,0.2f,1};
		float specularFlashLight[] = {1,1,0.8f,1};
		float ambientFlashLight[] = {1,1,1,0.2f};
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, diffuseFlashLight, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, specularFlashLight, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, ambientFlashLight, 0);
		//Position of flashLight
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, flashLightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, flashLightDirection, 0);
		//Cutoff angle
		gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, theta);
		
		//Enable the lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		//gl.glEnable(GL2.GL_LIGHT1);
		
		//If flash light is turned on
		if(flashLight) {
			gl.glEnable(GL2.GL_LIGHT2);			
		}
	
		//lets use use standard color functions
		//normalize the surface normals for lighting calculations
		gl.glEnable(GL2.GL_NORMALIZE);
	}
	
	//Draw spheres at the lights positions
	public void drawSpheres() {
		//Draw Sun sphere
		gl.glPushMatrix();
		gl.glTranslated(sunPos[0], sunPos[1], sunPos[2]);
			glut.glutSolidSphere(3, 10, 10);
		gl.glPopMatrix();
		
		//Draw Moon sphere
		gl.glPushMatrix();
		gl.glTranslated(moonPos[0], moonPos[1], moonPos[2]);
			glut.glutSolidSphere(3, 10, 10);
		gl.glPopMatrix();
	}
	
	//Update the the flash light every frame
	public void updateFlashLightPos(boolean status, float[] pos, float[] direction) {
		this.flashLight = status;
		this.flashLightPos = pos;
		this.flashLightDirection = direction;
	}
	
}
