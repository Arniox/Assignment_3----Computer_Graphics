package character;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import terrain.NoiseTerrain;

public class Player{
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Size
	private static final float SIZE = 1;
	
	//Location
	public float globalPosition[];
	public float heading;
	public float speed = 0.1f;
	public boolean movingForward = false;
	public boolean movingBackwards = false;
	public boolean strafeRight = false;
	public boolean strafeLeft = false;
	
	//Terrain
	NoiseTerrain terrain;
	
	//Constructor
	public Player(GL2 gl, GLUT glut, float[] globalStartPos, NoiseTerrain terrain) {
		//Set main variables
		this.gl = gl;
		this.glut = glut;
		
		//Set starter global position
		this.globalPosition = globalStartPos;
		this.heading = 0f;
		
		//Set terrain
		this.terrain = terrain;
	}
	
	//Draw
	public void drawPlayer() {
		//Push
		gl.glPushMatrix();
			//Translate
			gl.glTranslated(0, SIZE, 0);
			gl.glTranslated(globalPosition[0], globalPosition[1], globalPosition[2]);
			//Rotate
			gl.glRotated(heading, 0, 1, 0);
			
			//Draw the sphere
			glut.glutSolidSphere(SIZE, 50, 50);
			
		//Pop
		gl.glPopMatrix();
	}
	
	//Animate
	public void animate() {
		if(movingForward) {
			calculateMovement(speed, 0);
		}else if(movingBackwards) {
			calculateMovement(-speed, 0);
		}else if(strafeRight) {
			calculateMovement(speed, 90);
		}else if(strafeLeft) {
			calculateMovement(speed, -90);
		}
	}

	//Calculate movement
	private void calculateMovement(float distance, int alteration) {
		globalPosition[0] = globalPosition[0]+(float)Math.cos(heading+alteration) * distance;
		globalPosition[1] = terrain.getHeightBellow(globalPosition[0], globalPosition[2]);
		globalPosition[2] = globalPosition[2]+(float)Math.sin(heading+alteration) * distance;
	}
	
}
