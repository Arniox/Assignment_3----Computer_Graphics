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
		float ambientSun[] = { 1, 1, 1, 1 };
		float specularSun[] = { 1, 1, 1, 1 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientSun, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseSun, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularSun, 0);
		//Position of the sun
		sunPos = new float[]{0,100,0,1};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, sunPos, 0);
		
		//The moon
		float diffuseMoon[] = {0, 0, 1, 1};
		float ambientMoon[] = {0, 0, 1, 1};
		float specularMoon[] = {0, 0, 1, 1};
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambientMoon, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuseMoon, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specularMoon, 0);
		//Position of the moon
		moonPos = new float[]{0,-100,0,1};
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, moonPos, 0);
		
		//Enable the lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		//gl.glEnable(GL2.GL_LIGHT1);
	
		//lets use use standard color functions
		//gl.glEnable(GL2.GL_COLOR_MATERIAL);
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
	
}
