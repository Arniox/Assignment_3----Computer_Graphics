package lightAndCamera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class CoordinateAxes {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	/**
	 * Constructs the coordinate axis
	 * 
	 * @param gl - GL2 variable for the coordinate axis
	 * @param glut - GLUT variable for the coordinate axis
	 * 
	 * @author Nikkolas Diehl
	 */
	public CoordinateAxes(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	/**
	 * Draws the axis and debugging lines	 * 
	 * @author Nikkolas Diehl
	 */
	public void debug(float lengthAxis) {
		gl.glDisable(GL2.GL_LIGHTING);
		this.drawAxisLines(lengthAxis);
		this.drawCenterSphere();
		this.drawXYZText(lengthAxis);
	}
	
	/**
	 * Draw the axis lines for x, y, z, -x, -y, -z
	 * 
	 * @author Nikkolas Diehl
	 */
	private void drawAxisLines(float lengthAxis) {
		
		//Draw axis lines
		gl.glLineWidth(2.0f);
		gl.glBegin(GL2.GL_LINES);
		
		//Line Positive X - Red
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(lengthAxis, 0, 0);
		//Line Negative X - Red
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(-lengthAxis, 0, 0);
		//Line Positive Y - Green
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, lengthAxis, 0);
		//Line Negative Y - Green
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, -lengthAxis, 0);
		//Line Positive Z - Blue
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, lengthAxis);
		//Line Negative Z - Blue
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, -lengthAxis);

		gl.glEnd();
	}
	
	/**
	 * Draw the center sphere at 0,0,0
	 * 
	 * @author Nikkolas Diehl
	 */
	private void drawCenterSphere() {
		//Draw sphere
		gl.glColor3d(0, 0, 0);
		gl.glEnable(GL2.GL_LIGHTING);
		glut.glutSolidSphere(0.01, 50, 50);
	}
	
	/**
	 * Draw the x,y,z,-x,-y,-z text on the axis lines
	 * 
	 * @author Nikkolas Diehl
	 */
	private void drawXYZText(float lengthAxis) {
		//Draw text X - red
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glRasterPos3f(0.5f,0,0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "X");
		gl.glEnd();
		//Draw text -X - red
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glRasterPos3f(-0.5f,0,0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "-X");
		gl.glEnd();
		
		//Draw text Y - Green
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glRasterPos3f(0,0.5f,0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Y");
		gl.glEnd();
		//Draw text -Y - Green
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glRasterPos3f(0,-0.5f,0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "-Y");
		gl.glEnd();
		
		//Draw text Z - Blue
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glRasterPos3f(0,0,0.5f);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Z");
		gl.glEnd();
		//Draw text -Z - Blue
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glRasterPos3f(0,0,-0.5f);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "-Z");
		gl.glEnd();
	}
}
