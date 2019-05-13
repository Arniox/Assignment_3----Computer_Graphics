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
	
	//Material
	//Player materials (constant)
	private static final float GRASS_DIFFUSE[] = {0.94f,0.90f,0.549f,1};
	private static final float GRASS_SPECULAR[] = {1,1,1,1};
	private static final float GRASS_AMBIENT[] = {0.094f,0.090f,0.0549f,0.8f};
	private static final float GRASS_SHININESS = 100;
	
	//Constructor
	public Player(GL2 gl, GLUT glut, NoiseTerrain terrain) {
		//Set main variables
		this.gl = gl;
		this.glut = glut;
		
		//Set starter global position
		this.globalPosition = new float[]{0,0,0};
		this.heading = 0f;
		
		//Set terrain
		this.terrain = terrain;
	}
	
	//Draw
	public void drawPlayer(int frame) {
		if(frame==0) {
			globalPosition[1] = terrain.getHeightBellow(globalPosition[0], globalPosition[2]);
		}
		
		//Push
		gl.glPushMatrix();
			//Translate
			gl.glTranslated(0, SIZE, 0);
			gl.glTranslated(globalPosition[0], globalPosition[1], globalPosition[2]);
			//Rotate
			gl.glRotated(heading, 0, 1, 0);
			
			//Set material:
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, GRASS_AMBIENT, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, GRASS_DIFFUSE, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, GRASS_SPECULAR, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, GRASS_SHININESS);
			
			//Draw the sphere
			glut.glutSolidSphere(SIZE, 100, 100);
			
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
		globalPosition[0] = globalPosition[0]+(float)Math.cos(Math.toRadians(heading+alteration)) * distance;
		globalPosition[1] = terrain.getHeightBellow(globalPosition[0], globalPosition[2]);
		globalPosition[2] = globalPosition[2]+(float)Math.sin(Math.toRadians(heading+alteration)) * distance;
	}
	
}
