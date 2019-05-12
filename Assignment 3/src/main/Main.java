package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import character.Player;
import lightAndCamera.CoordinateAxes;
import lightAndCamera.Lighting;
import lightAndCamera.TrackballCamera;
import terrain.NoiseTerrain;

public class Main implements GLEventListener, KeyListener{
	//Main variables
	private static GLCanvas canvas;
	private static GL2 gl;
	private static GLUT glut;
	
	//Display Lists
	private int displayList;
	
	//Terrain
	private NoiseTerrain terrain;
	
	//Character
	private Player player;
	
	//Lighting
	private Lighting lighting;
	
	//Current camera mode
	private int cameraInUse = 0;
	//Debugging
	private boolean debugging = true;
	private boolean wireFrame = true;
	
	//Animate
	private boolean animate = true;
	
	//Trackball camera for debugging
	private TrackballCamera debuggingCamera;
	//Coordinate axis for debugging
	private CoordinateAxes coordinateAxis;
	
	public static void main(String[] args){
		//Build GUIView which contains everything
		Frame frame = new Frame("Amazing Game Name");
		
		//Set up GL info
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		
		//Canvas stuff
		Main gameWindow = new Main();
		canvas.addGLEventListener(gameWindow);
		canvas.addKeyListener(gameWindow);
		frame.add(canvas);
		
		//Scaling setup
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)(screenSize.getWidth()/1.2), (int)(screenSize.getHeight()/1.2));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(false);
		frame.setResizable(true);
		
		//Animator
		final FPSAnimator animator = new FPSAnimator(canvas, 60);
		
		//Window Listener
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		
		//Last setup
		animator.start();
		canvas.requestFocusInWindow();
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		//Set up main functions
		this.setMainFunctions();
		
		//Drawing
		//Camera drawing
		switch(cameraInUse){
			case 0:
				debuggingCamera.draw(gl, true);
				break;
			case 1:
				break;
			case 2:
				break;
		}
		//Axis drawing
		if(debugging) {
			coordinateAxis.debug(100, displayList);
		}
		
		//Draw all other non transparent objects
		this.terrain.drawHeightMappedTerrain(displayList+1);
		this.player.drawPlayer();
		this.lighting.drawSpheres();
		//Draw transparent objects last
		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask(false);
			//Draw
		gl.glDepthMask(true);
		gl.glDisable(GL2.GL_BLEND);
		
		//Animate
		if(animate) {
			player.animate();
		}
		
		//Call all display lists
		gl.glCallList(displayList);
		gl.glCallList(displayList+1);
		
		gl.glFlush();
		
		//Delete display lists
		gl.glDeleteLists(displayList, 1);
		gl.glDeleteLists(displayList+1, 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("Press Comma (,): Toggle debugging\n"+
						   "Press Back Slash (\\): Toggle wire frame\n"+
						   "Press Space: Toggle animation\n"+
						   "Press 1: Debugging Camera\n"+
						   "");
		
		//Set up mains
		gl = drawable.getGL().getGL2();
		glut = new GLUT();
		
		//enable V-sync
		gl.setSwapInterval(1);
		
		//Set up the drawing area and shading mode
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		//use the lights
		lighting = new Lighting(gl, glut);
		lighting.useLighting();
		gl.glShadeModel(GL2.GL_SMOOTH);
		
		//Set up cameras
		debuggingCamera = new TrackballCamera(canvas);
		debuggingCamera.setDistance(1.5);
		debuggingCamera.setFieldOfView(70);
		debuggingCamera.setLookAt(0, 0, 0);
		
		//Display lists 
		displayList = gl.glGenLists(2);
		
		//Create Axis
		coordinateAxis = new CoordinateAxes(gl, glut);
		
		//Create all objects
		terrain = new NoiseTerrain(gl);
		
		//Set character
		float x = 0;
		float z = 0;
		float[] pStart = new float[]{x-1, terrain.getHeightBellow(x-1, z-1), z-1};
		player = new Player(gl, glut, new float[]{pStart[0], pStart[1], pStart[2]}, this.terrain);
	}
	
	//Set up main functions
	public void setMainFunctions() {
		//Drawing mode
		if(wireFrame) {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		}else {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		
		//Select and clear model-view matrix
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glLoadIdentity();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		debuggingCamera.newWindowSize(width, height);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_COMMA) {
			this.debugging = !this.debugging;
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
			this.wireFrame = !this.wireFrame;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.animate = !this.animate;
		}
		if(e.getKeyCode() == KeyEvent.VK_1) {
			this.cameraInUse = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			this.cameraInUse = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			this.cameraInUse = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.player.movingForward = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			this.player.movingBackwards = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.player.strafeLeft = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.player.strafeRight = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.player.movingForward = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			this.player.movingBackwards = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.player.strafeLeft = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.player.strafeRight = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

}
