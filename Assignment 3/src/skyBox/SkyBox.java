package skyBox;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;

public class SkyBox {
	//Maion variables
	private GL2 gl;
	private GLUT glut;
	
	//Vertices
	private int[][] vertices;
	private int[][] faces;
	private int[][] normals;
	private int[][] textureCoords;
	
	//Textures
	public Texture[] textures;
	private Texture tex;
	
	//Material
	private static final float SKY_DIFFUSE[] = {1,1,1,1};
	private static final float SKY_SPECULAR[] = {0,0,0,0};
	private static final float SKY_AMBIENT[] = {0.3f,0.3f,0.3f,1};
	private static final float SKY_SHININESS = 0;
	
	//Constructor
	public SkyBox(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
		
		textures = new Texture[3];
		generateShape();
	}
	
	//Draw
	public void drawSkyBox(float[] characterPos) {		
		//Push and pop
		gl.glPushMatrix();
		gl.glTranslated(characterPos[0], 50, characterPos[2]);
		
		//Load
		tex = textures[0];
		//Bind
		tex.enable(gl);
		tex.bind(gl);
		//Set clamping (Set clamping for s and then t)
		tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		//Set material:
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, SKY_DIFFUSE, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, SKY_SPECULAR, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, SKY_AMBIENT, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, SKY_SHININESS);
				
		//Draw cube
		gl.glBegin(GL2.GL_QUADS);
		int faceI = 0;
		for(int[] face : faces) {
			for(int vertexNo : face) {
				gl.glNormal3iv(normals[faceI],0);
				gl.glVertex3iv(vertices[vertexNo], 0);
			}
			
			faceI++;
		}
		gl.glEnd();

		//Pop
		gl.glPopMatrix();
	}
	
	//Generate vertices
	private void generateShape() {
		this.vertices = new int[][]{
			{-100,-100, 100},{ 100,-100, 100},
			{ 100, 100, 100},{-100, 100, 100},
			{-100,-100,-100},{ 100,-100,-100},
			{ 100, 100,-100},{-100, 100,-100}
		};
		this.faces = new int[][] {
			{3,2,1,0},{4,5,6,7},
			{0,4,7,3},{2,6,5,1},
			{0,1,5,4},{7,6,2,3}
		};
		this.normals = new int[][] {
			{ 0, 0,-1}, { 0, 0,-1},
			{ 1, 0, 0}, {-1, 0, 0},
			{ 0, 1, 0}, { 0,-1, 0}
		};
	}
	
	//Calculate normal
	private float[] calculateNormals(int[] point0, int[] point1, int[] point2) {
		float[] normal = new float[3];
		float[] U = new float[3];
		float[] V = new float[3];
		
		//Set U
		U[0] = point1[0]-point0[0];
		U[1] = point1[1]-point0[1];
		U[2] = point1[2]-point0[2];
		
		//Set V
		V[0] = point2[0]-point0[0];
		V[1] = point2[1]-point0[1];
		V[2] = point2[2]-point0[2];
		
		normal[0] = U[0]*V[0];
		normal[1] = U[1]*V[1];
		normal[2] = U[1]*V[1];
		
		return normal;
	}
}
