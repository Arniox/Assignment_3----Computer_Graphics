package lightAndCamera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Lighting {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Consturctor
	public Lighting(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	public void useLighting() {
		// lighting stuff
		float ambient[] = { 1, 1, 1, 1 };
		float diffuse[] = {1f, 1f, 1f, 1 };
		float specular[] = { 1, 1, 1, 1 };
		
		float[] ambientLight = { 0.4f, 0.4f, 0.4f, 0};  // weak RED ambient 
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0); 
		
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	
		//lets use use standard color functions
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		//normalise the surface normals for lighting calculations
		gl.glEnable(GL2.GL_NORMALIZE);
	}
	
}
