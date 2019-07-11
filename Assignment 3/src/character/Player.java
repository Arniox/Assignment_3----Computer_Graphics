package character;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.gl2.GLUT;

import lightAndCamera.FirstPersonCamera;
import lightAndCamera.FlashLight;
import terrain.NoiseTerrain;

public class Player implements KeyListener, MouseMotionListener{
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Size
	private static final float SIZE = 1;
	private static float[] frameDimension;
	
	//Flashlight and camera
	private FlashLight flashLight;
	public FirstPersonCamera povCamera;
	
	//Location
	public float globalPosition[];
	public float speed = 0;
	private float movementSpeedSet = 0.1f;
	public float alteration = 0;
	
	//Ratios
	private static final float MAX_PITCH = 90f;
	private static final float MIN_PITCH = -45f;
	public float xHeading = 0;
	public float yHeading = 0;
	public float rotation = 0;
	
	//Movements
	private boolean moveForward = false;
	private boolean moveBackward = false;
	private boolean strafeLeft = false;
	private boolean strafeRight = false;
	private boolean rotateRight = false;
	private boolean rotateLeft = false;
	private boolean pitchUp = false;
	private boolean pitchDown = false;
	
	//Terrain
	NoiseTerrain terrain;
	
	//Material
	//Player materials (constant)
	private static final float GRASS_DIFFUSE[] = {0.94f,0.90f,0.549f,1};
	private static final float GRASS_SPECULAR[] = {1,1,1,1};
	private static final float GRASS_AMBIENT[] = {0.094f,0.090f,0.0549f,0.8f};
	private static final float GRASS_SHININESS = 100;
	
	//Constructor
	public Player(GLCanvas canvas, float[] frameDim, GL2 gl, GLUT glut, NoiseTerrain terrain) {
		//Set canvas
		canvas.addKeyListener(this);
		canvas.addMouseMotionListener(this);
		
		//Set main variables
		this.gl = gl;
		this.glut = glut;
		
		frameDimension = frameDim;
		
		//Set terrain
		this.terrain = terrain;
		
		//Set starter global position
		float x = (float)Math.random()*(terrain.getSize()/2)-terrain.getSize()/4;
		float z = (float)Math.random()*(terrain.getSize()/2)-terrain.getSize()/4;
		this.globalPosition = new float[]{x,terrain.getHeightBellow(x, z),z};
		
		//Set up flashlight
		flashLight = new FlashLight(gl, globalPosition, xHeading, yHeading);
		flashLight.initFlashLight();
		
		//Set up camera
		povCamera = new FirstPersonCamera();
		povCamera.setFieldOfView(70);
		povCamera.setLookAt(xHeading, yHeading, globalPosition);
	}
	
	//Draw
	public void drawPlayer(int frame, boolean flashLightStatus) {
		if(frame==0) {
			globalPosition[1] = terrain.getHeightBellow(globalPosition[0], globalPosition[2]);
		}
		gl.glShadeModel(GL2.GL_SMOOTH);
		//Push
		gl.glPushMatrix();
			//Translate
			gl.glTranslated(0, SIZE, 0);
			gl.glTranslated(globalPosition[0], globalPosition[1], globalPosition[2]);
			//Rotated
			gl.glRotated(xHeading, 0, 1, 0);
			
			//Set material:
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, GRASS_AMBIENT, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, GRASS_DIFFUSE, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, GRASS_SPECULAR, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, GRASS_SHININESS);
		
			//Draw the sphere
			glut.glutSolidCube(SIZE);
			
			//Flashlight
			gl.glRotated(yHeading/2, 0, 0, 1);
			flashLight.runOnDisplay(flashLightStatus);
			
		//Pop
		gl.glPopMatrix();
	}
	
	//Sprint
	public void sprint(boolean sprint) {
		if(sprint) {
			movementSpeedSet = 0.3f;
		}else {
			movementSpeedSet = 0.1f;
		}
	}
	
	//Draw camera
	public void drawPlayerCamera() {
		//Camera
		povCamera.draw(gl, globalPosition, xHeading, yHeading);
	}
	
	//Animate
	public void animate() {
		if(moveForward) {
			calculateMovement(movementSpeedSet, 0);
		}
		if(moveBackward) {
			calculateMovement(-movementSpeedSet, 0);
		}
		if(strafeLeft) {
			calculateMovement(movementSpeedSet, -90);
		}
		if(strafeRight) {
			calculateMovement(movementSpeedSet, 90);
		}
		if(rotateRight) {
			calculateRotation(-3f);
		}
		if(rotateLeft) {
			calculateRotation(3f);
		}
		if(pitchUp) {
			calculatePitch(5f);
		}
		if(pitchDown) {
			calculatePitch(-5f);
		}
	}

	//Calculate movement
	private void calculateMovement(float distance, float alteration) {
		globalPosition[0] = globalPosition[0]+(float)Math.cos(Math.toRadians(-xHeading+alteration)) * distance;
		globalPosition[1] = terrain.getHeightBellow(globalPosition[0], globalPosition[2]);
		globalPosition[2] = globalPosition[2]+(float)Math.sin(Math.toRadians(-xHeading+alteration)) * distance;
	}
	
	//Calculate rotation
	private void calculateRotation(float addition) {
		xHeading += addition;
	}
	//Caculate pitch
	private void calculatePitch(float addition) {
		if(yHeading > MAX_PITCH || yHeading < MIN_PITCH) {
			if(yHeading > MAX_PITCH && pitchDown) {
				yHeading += addition;
			}else if(yHeading < MIN_PITCH && pitchUp) {
				yHeading += addition;
			}
		}else {
			yHeading += addition;
		}
	}
	
	//Close
	public void close() {
		this.flashLight.close();
		this.flashLight = null;
		this.gl = null;
		this.glut = null;
		this.povCamera.close();
		this.povCamera = null;
		System.out.println("Player closed....");
	}
	
	//Key Events
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) { moveForward = true; }
		if(e.getKeyCode() == KeyEvent.VK_S) { moveBackward = true; }
		if(e.getKeyCode() == KeyEvent.VK_A) { strafeLeft = true; }
		if(e.getKeyCode() == KeyEvent.VK_D) { strafeRight = true; }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) { moveForward = false; }
		if(e.getKeyCode() == KeyEvent.VK_S) { moveBackward = false; }
		if(e.getKeyCode() == KeyEvent.VK_A) { strafeLeft = false; }
		if(e.getKeyCode() == KeyEvent.VK_D) { strafeRight = false; }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		float x = convertXtoOpenGl(e.getX());
		float y = convertYtoOpenGl(e.getY());
		
		//if X
		if(x > 0.35f) {
			rotateRight = true;
			rotateLeft = false;
		}else if(x < -0.35f) {
			rotateRight = false;
			rotateLeft = true;
		}else {
			rotateRight = false;
			rotateLeft = false;
		}
		
		//if Y
		if(y > 0.35f) {
			pitchUp = true;
			pitchDown = false;
		}else if(y < -0.35f) {
			pitchUp = false;
			pitchDown = true;
		}else {
			pitchUp = false;
			pitchDown = false;
		}
	}
	
	//Convert Java position to openGl position X
	public float convertXtoOpenGl(float x) {
		float xOpengl = 2.0f * (x/frameDimension[0]) - 1.0f;
		return xOpengl;
	}
	//Convert Java position to openGl position Y
	public float convertYtoOpenGl(float y) {
		float invY = frameDimension[1] - y;
		float yOpengl = 2.0f * (invY/frameDimension[1]) - 1.0f;
		return yOpengl;
	}
	
	//Unused
	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	
	
}
