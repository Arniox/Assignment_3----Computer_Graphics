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
			coordinateAxis.debug();
		}
		
		//Draw all other non transparent objects
			this.flatTerrain.drawTerrain(4, 4, 10);
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
		flatTerrain = new FlatTerrain(this.gl, this.glut);	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		debuggingCamera.newWindowSize(width, height);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

}
