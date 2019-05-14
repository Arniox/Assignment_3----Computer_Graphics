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
}
