package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL;
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
import shaders.StaticShader;
import skyBox.SkyBox;
import terrain.NoiseTerrain;
import textures.TextureLoader;

public class Main implements GLEventListener{
	//Main variables
	public static GLCanvas canvas;
	public static GL2 gl;
	public static GLUT glut;
	public static StaticShader shader;
	
	//Display Lists
	public int displayList;
	
	//Terrain
	public NoiseTerrain terrain;
	//Sky box
	public SkyBox skyBox;
	
	//Texturing
	public TextureLoader textureLoader;
	public static final String[] TERRAIN_TEXTURES = new String[]{"textures/grass.jpg",
																 "textures/pebbles.jpg",
																 "textures/groundcover.jpg",
																 "textures/snow.jpg",
																 "textures/All.png"};
	public static final String[] SKY_BOX_TEXTURES = new String[]{"textures/clouds.png",
																 "textures/day.png",
																 "textures/night.png"};
	
	//Character
	public Player player;
	
	//Lighting
	public Lighting lighting;
	
	//Current camera mode
	public int cameraInUse = 0;
	//Debugging
	public boolean debugging = true;
	public boolean wireFrame = false;
	
	//Timing
	private int frame = 0;
	
	//Animate
	public boolean animate = true;
	
	//Trackball camera for debugging
	public TrackballCamera debuggingCamera;
	//Coordinate axis for debugging
	public CoordinateAxes coordinateAxis;
	
	public static void main(String[] args){
		//Build GUIView which contains everything
		Frame frame = new Frame("Amazing Game Name");
		
		//Set up GL info
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		
		//Canvas stuff
		Main gameWindow = new Main();
		EventHandler eventHandler = new EventHandler(gameWindow);
		canvas.addGLEventListener(gameWindow);
		canvas.addKeyListener(eventHandler);
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
						
						shader.cleanUp();
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
		this.setMainFunctionsDisplay();
		//Shader
		shader.useProgram();
		
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
		this.player.drawPlayer(frame);
		this.skyBox.drawSkyBox(player.globalPosition);
		this.lighting.drawSpheres();
		//Draw transparent objects last
		gl.glDepthMask(false);
			//Draw
		gl.glDepthMask(true);
		
		//Animate
		if(animate) {
			player.animate();
		}
		
		//Call all display lists
		gl.glCallList(displayList);
		gl.glCallList(displayList+1);

		gl.glDisable(GL2.GL_BLEND);
		gl.glFlush();
		
		//Delete display lists
		gl.glDeleteLists(displayList, 1);
		gl.glDeleteLists(displayList+1, 1);
		
		shader.stop();
		
		//Iterate frame
		frame++;
	}

	@Override
	public void init(GLAutoDrawable drawable) {	
		System.out.println("Loading...");
		//Set up mains
		gl = drawable.getGL().getGL2();
		glut = new GLUT();
		shader = new StaticShader(gl);
		textureLoader = new TextureLoader();
		setMainFunctionsInit();
		
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
		skyBox = new SkyBox(gl, glut);
		//Load up textures
		loadTextures();
		
		//Set character
		player = new Player(gl, glut, this.terrain);
	}
	
	//Load up textures
	public void loadTextures() {
		int k = 1;
		float totalLength = (float)TERRAIN_TEXTURES.length + SKY_BOX_TEXTURES.length;
		
		//Load terrain textures
		for(int i=0; i<TERRAIN_TEXTURES.length; i++) {
			terrain.textures[i] = textureLoader.loadTexture(TERRAIN_TEXTURES[i]);
			System.out.println("[DEBUG] - "+(((float)k/totalLength)*100)+"% loaded...");
			
			if(terrain.textures[i] != null) {
				System.out.println("[DEBUG] - "+TERRAIN_TEXTURES[i]+" BUFFERED correctly - "+terrain.textures[i].getWidth()+" x "+terrain.textures[i].getHeight());
			}else {
				System.out.println("[DEBUG] - "+TERRAIN_TEXTURES[i]+" FAILED to buffer correctly");			
			}
			
			k++;
		}
		
		//Load skybox textures
		for(int i=0; i<SKY_BOX_TEXTURES.length; i++) {
			skyBox.textures[i] = textureLoader.loadTexture(SKY_BOX_TEXTURES[i]);
			System.out.println("[DEBUG] - "+(((float)k/totalLength)*100)+"% loaded...");
			
			if(skyBox.textures[i] != null) {
				System.out.println("[DEBUG] - "+SKY_BOX_TEXTURES[i]+" BUFFERED correctly - "+skyBox.textures[i].getWidth()+" x "+skyBox.textures[i].getHeight());
			}else {
				System.out.println("[DEBUG] - "+SKY_BOX_TEXTURES[i]+" FAILED to buffer correctly");
			}
			
			k++;
		}
	}
	
	//Set up main functions for display
	public void setMainFunctionsDisplay() {
		//Drawing mode
		if(wireFrame) {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		}else {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		
		//Select and clear model-view matrix
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glLoadIdentity();
	}
	
	//Set up main functions for init
	public void setMainFunctionsInit() {
		//enable V-sync
		gl.setSwapInterval(1);
		
		//Set up the drawing area and shading mode
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glCullFace(GL.GL_BACK);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		debuggingCamera.newWindowSize(width, height);
	}
	

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

}
