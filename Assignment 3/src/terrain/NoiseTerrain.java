package terrain;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

//Textures from - https://www.wildtextures.com/category/options/seamless/
public class NoiseTerrain {
	//Main variables
	private GL2 gl;

	//Height map
	float[][] heightMap = null;
	
	//Terrain size
	private static final int SIZE = 500;
	
	//Height Generator
	private HeightsGenerator generator;
	
	//Texturing
	public Texture[] textures;
	private Texture tex;
	public String[] textureNames;
	
	//Material
	//Grass terrain materials (constant)
	//Diffuse - Light that hits the surface of an object - 
	//			//The 'color' that defines a shape. If a shape has material of blood, it would reflect a diffuse of {1,0,0,1} or have
	//			//a red texture with a diffuse of {1,1,1,1}
	//Specular - Directional light that reflects off of a surface in a sharp and uniform way
	//			//The light that bounces off an object. This will almost always be white {1,1,1,1} or no light {0,0,0,0}
	
	private static final float GRASS_DIFFUSE[] = {1,1,1,1};
	private static final float GRASS_SPECULAR[] = {0,0,0,0};
	private static final float GRASS_AMBIENT[] = {0,0,0,0};
	private static final float GRASS_SHININESS = 0;
	
	//Constructor
	public NoiseTerrain(GL2 gl) {
		//Main variables
		this.gl = gl;
		
		//Heights
		this.heightMap = new float[SIZE+1][SIZE+1];
		this.generator = new HeightsGenerator();
		this.generateHeights();
		
		//Fill texture array
		textures = new Texture[4];	
		textureNames = new String[]{"textures/grass.jpg",
									"textures/pebbles.jpg",
									"textures/groundcover.jpg",
									"textures/snow.jpg"};
	}
	
	//Draw
	public void drawHeightMappedTerrain(int displayList) {
		//Push and pop
		gl.glPushMatrix();
		gl.glTranslated(-(SIZE/2), 0, -(SIZE/2));
		
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
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, GRASS_AMBIENT, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, GRASS_DIFFUSE, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, GRASS_SPECULAR, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, GRASS_SHININESS);
		
		//Set terrain for height map
		for(int z=0; z<SIZE; z++) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for(int x=0; x<SIZE; x++) {
				gl.glNormal3fv(this.calculateNormal(x, z),0);
				gl.glTexCoord2d(x, z);
					gl.glVertex3f(x, heightMap[z][x], z);
					
				gl.glNormal3fv(this.calculateNormal(x, z+1),0);
				gl.glTexCoord2d(x, z+1);
					gl.glVertex3f(x, heightMap[z+1][x], z+1);
			}
			gl.glEnd();
		}
		tex.disable(gl);
		//Pop
		gl.glPopMatrix();
	}
	
	//Get texture at point
	private void setTexture(int x, float y, int z) {
		if(y>10) {
			tex.disable(gl);
			tex = textures[3];
			tex.enable(gl);
			tex.bind(gl);
		}else {
			tex.disable(gl);
			tex = textures[0];
			tex.enable(gl);
			tex.bind(gl);			
		}
	}
	
	//Generate heights
	private void generateHeights() {
		for(int z=0; z<SIZE+1; z++) {
			for(int x=0; x<SIZE+1; x++) {
				heightMap[z][x] = generator.generateHeight(x, z);
			}
		}

		System.out.println("[DEBUG] - Height at 0, 0 is: "+heightMap[0][0]);
	}
	
	//Calculate normal
	private float[] calculateNormal(int x, int z){
		//Get nearest neighbor heights
		float hL = getHeight(x-1, z);
		float hL2 = getHeight(x-2, z);
		float hR = getHeight(x+1, z);
		float hR2 = getHeight(x+2, z);
		float hD = getHeight(x, z-1);
		float hD2 = getHeight(x, z-2);
		float hU = getHeight(x, z+1);
		float hU2 = getHeight(x, z+2);
		
		//deduce normal
		float[] normal = new float[]{((hL2-hL)-(hR2-hR)),2f,((hD2-hD)-(hU2-hU))};
		return normal;
	}
	
	//Get height
	private float getHeight(int x, int z) {
		try {
			return heightMap[z][x];
		}catch(Exception e) {
			return 0;
		}
	}
	
	//Get translated height
	public float getHeightBellow(float x, float z) {
		try {
			return heightMap[(int)z+SIZE/2][(int)x+SIZE/2];
		}catch(Exception e) {
			return 0;
		}
	}
	
	//Buffer textures
	public boolean bufferTextures(int i) {
		//Buffer in texture at textureName[i]
		try {
			textures[i] = TextureIO.newTexture(new File(textureNames[i]), true);
			textures[i].isUsingAutoMipmapGeneration();
			
			if(textures[i] != null) {
				return true;
			}
		} catch (GLException | IOException e) {
			return false;
		}
		
		return false;
	}
	
}
