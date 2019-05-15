package terrain;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import loader.TextureLoader;
import shaders.StaticShader;

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
	private TextureLoader textureLoader;
	private Texture[] textures;
	private Texture tex1;
	private Texture tex2;
	public static final String[] TERRAIN_TEXTURES = new String[]{"textures/grass.jpg",
																 "textures/pebbles.jpg",
																 "textures/groundcover.jpg",
																 "textures/snow.jpg"};
	
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
		textureLoader = new TextureLoader();
		textures = new Texture[4];
		loadTextures();
	}
	
	//Draw
	public void drawHeightMappedTerrain(StaticShader shader) {
		//Push and pop
		gl.glPushMatrix();
		gl.glTranslated(-(SIZE/2), 0, -(SIZE/2));
		
		//Load
		tex1 = textures[0];
		tex2 = textures[3];
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		tex1.bind(gl);
		tex1.enable(gl);
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		tex2.bind(gl);
		tex2.enable(gl);
		
		//Set texture parameter
		this.setTextureParameter();
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
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, x, z);
					gl.glVertex3f(x, heightMap[z][x], z);
					
				gl.glNormal3fv(this.calculateNormal(x, z+1),0);			
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, x, z+1);
					gl.glVertex3f(x, heightMap[z+1][x], z+1);
			}
			gl.glEnd();
		}

		//Disable
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		tex1.disable(gl);
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		tex2.disable(gl);
		
		//Pop
		gl.glPopMatrix();
	}
	
	//Load textures
	private void loadTextures() {
		System.out.println("\n[DEBUG] - Loading Terrain Textures...");
		
		//Load terrain textures
		for(int i=0; i<TERRAIN_TEXTURES.length; i++) {
			textures[i] = textureLoader.loadTexture(TERRAIN_TEXTURES[i]);
			System.out.println("[DEBUG] - "+(i+1)+"/"+TERRAIN_TEXTURES.length+" loaded...");
			
			if(textures[i] != null) {
				System.out.println("[DEBUG] - "+TERRAIN_TEXTURES[i]+" BUFFERED correctly - "+textures[i].getWidth()+" x "+textures[i].getHeight());
			}else {
				System.out.println("[DEBUG] - "+TERRAIN_TEXTURES[i]+" FAILED to buffer correctly");
			}
		}
	}
	
	//Set texture parameter
	private void setTextureParameter() {
		//Set tex1
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		
		//Set tex2
		tex2.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		tex2.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		tex2.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		tex2.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
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
	
}
