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

import lightAndCamera.CoordinateAxes;
import lightAndCamera.TrackballCamera;
import terrain.FlatTerrain;

public class Main implements GLEventListener, KeyListener{
	//Main variables
	private static GLCanvas canvas;
	private static GL2 gl;
	private static GLUT glut;	
	
	//Terrain
	private FlatTerrain flatTerrain;
	
	//Current camera mode
	private int cameraInUse = 0;
	//Debugging
	private boolean debugging = true;
	private boolean wireFrame = true;
	
	//Terrain generation
	private float terrainHeightRange = 1f;
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
		final FPSAnimator animator = new FPSAnimator(canvas, 165);
		
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
		//Drawing mode
		if(wireFrame) {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		}else {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		
		//Select and clear model-view matrix
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
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
			coordinateAxis.debug(100);
		}
		
		//Draw all other non transparent objects
			this.flatTerrain.drawTerrain();
		//Draw transparent objects last
		gl.glEnable(GL2.GL_BLEND);
		gl.glDepthMask(false);
			//Draw
		gl.glDepthMask(true);
		gl.glDisable(GL2.GL_BLEND);
		
		//Animate
		if(animate) {
			
		}
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("Press D: Toggle debugging\n"+
						   "Press W: Toggle wire frame\n"+
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
		gl.glShadeModel(GL2.GL_SMOOTH);
		
		//Set up cameras
		debuggingCamera = new TrackballCamera(canvas);
		debuggingCamera.setDistance(1.5);
		debuggingCamera.setFieldOfView(70);
		debuggingCamera.setLookAt(0, 0, 0);
		
		//Create Axis
		coordinateAxis = new CoordinateAxes(gl, glut);
		
		//Create all objects
		//Detail must be a non negative
		flatTerrain = new FlatTerrain(this.gl, this.glut, 1000, 1000, 100, terrainHeightRange);
		
		//use the lights
		this.lights(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		debuggingCamera.newWindowSize(width, height);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.debugging = !this.debugging;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}
	
	//Basic lighting
	private void lights(GL2 gl) {
		// lighting stuff
		float ambient[] = { 0, 0, 0, 1 };
		float diffuse[] = {1f, 1f, 1f, 1 };
		float specular[] = { 1, 1, 1, 1 };

		float[] ambientLight = { 0.1f, 0.1f, 0.1f,0f };  // weak RED ambient
		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, ambientLight, 0);

		float position0[] = { 5, 5, 5, 0 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);

		float position1[] = { -10, -10, -10, 0 };
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular, 0);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);

		//lets use use standard color functions
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		//normalise the surface normals for lighting calculations
		gl.glEnable(GL2.GL_NORMALIZE);
	}

}
