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
	private static final int SIZE = 1200;
	
	//Height Generator
	private HeightsGenerator generator;
	
	//Texturing
	private TextureLoader textureLoader;
	private Texture texture;
	
	//Material
	//Grass terrain materials (constant)
	//Diffuse - Light that hits the surface of an object - 
	//			//The 'color' that defines a shape. If a shape has material of blood, it would reflect a diffuse of {1,0,0,1} or have
	//			//a red texture with a diffuse of {1,1,1,1}
	//Secular - Directional light that reflects off of a surface in a sharp and uniform way
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
		this.heightMap = new float[SIZE+3][SIZE+3];
		this.generator = new HeightsGenerator();
		System.out.println("[DEBUG] - Generating Height Map with SIZE: "+(SIZE+3)+" by "+(SIZE+3));
		this.generateHeights();
		
		//Fill texture array
		textureLoader = new TextureLoader();
		loadTextures();
	}
	
	//Draw
	public void drawHeightMappedTerrain(StaticShader shader, float[] playerPosition, float skyBoxSize) {
		gl.glShadeModel(GL2.GL_SMOOTH);
		//Push and pop
		gl.glPushMatrix();
		gl.glTranslated(-(SIZE/2), 0, -(SIZE/2));
		
		//Load
		texture.bind(gl);
		texture.enable(gl);
		
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
				//Only draw the terrain if it's within range of the player to save on memory
				if(!(getDistance(new float[]{x,heightMap[z][x],z}, playerPosition)>(skyBoxSize*1.4))) {
					gl.glNormal3fv(this.calculateNormal(x, z),0);
					gl.glTexCoord2f((float)x/4f, z);
						gl.glVertex3f(x, heightMap[z][x], z);
						
					gl.glNormal3fv(this.calculateNormal(x, z+1),0);	
					gl.glTexCoord2f((float)x/4f, z+1);
						gl.glVertex3f(x, heightMap[z+1][x], z+1);
				}
			}
			gl.glEnd();
		}

		//Disable
		texture.disable(gl);
		
		//Pop
		gl.glPopMatrix();
	}
	
	//Get distance
	private float getDistance(float[] input1, float[] input2) {
		//Make values all absolute
		float[] point1 = new float[3];
		float[] point2 = new float[3];
		//Point 1
		point1[0] = Math.abs(input1[0]);
		point1[1] = Math.abs(input1[2]);
		point1[2] = Math.abs(input1[2]);
		//Point 2
		point2[0] = Math.abs(input2[0]+SIZE/2);
		point2[1] = Math.abs(input2[2]);
		point2[2] = Math.abs(input2[2]+SIZE/2);
		
		return (float)Math.sqrt(Math.pow((point2[0]-point1[0]), 2)+Math.pow((point2[2]-point1[2]), 2));
	}
	
	//Load textures
	private void loadTextures() {
		System.out.println("\n[DEBUG] - Loading Terrain Textures...");
		
		//Load terrain textures
		texture = textureLoader.loadTexture("textures/terrain/grass.jpg");
		
		if(texture != null) {
			System.out.println("[DEBUG] - textures/terrain/grass.jpg BUFFERED correctly - "+texture.getWidth()+" x "+texture.getHeight());
		}else {
			System.out.println("[DEBUG] - textures/terrain/grass.jpg FAILED to buffer correctly");
		}
	}
	
	//Set texture parameter
	private void setTextureParameter() {
		//Set tex1
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
	}
	
	//Generate heights
	private void generateHeights() {
		for(int z=0; z<SIZE+3; z++) {
			for(int x=0; x<SIZE+3; x++) {
				heightMap[z][x] = generator.generateHeight(x, z);
			}
		}

		System.out.println("[DEBUG] - Finished Generating Height Map: "+(SIZE+3)+" by "+(SIZE+3));
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
	
	//Get terrain size
	public int getSize() {
		return NoiseTerrain.SIZE;
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
			float y = this.getBarycenticCoords(
				new float[]{(int)x, heightMap[(int)z+SIZE/2][(int)x+SIZE/2], (int)z}, 
				new float[]{(int)x, heightMap[((int)z+SIZE/2)+1][(int)x+SIZE/2], (int)z+1},
				new float[]{(int)x+1, heightMap[((int)z+SIZE/2)+1][((int)x+SIZE/2)+1], (int)z+1},
				x, z
			);
			
			return y;
		}catch(Exception e) {
			return 0;
		}
	}
	
	//Get barycentric coordinates
	private float getBarycenticCoords(float[] p1, float[] p2, float[] p3, float x, float z) {
		float det = (p2[2] - p3[2]) * (p1[0] - p3[0]) + (p3[0] - p2[0]) * (p1[2] - p3[2]);
		
		if(det==0) {
			return 0;
		}
		
		float I1 = ((p2[2] - p3[2]) * (x - p3[0]) + (p3[0] - p2[0]) * (z - p3[2])) / det;
		float I2 = ((p3[2] - p1[2]) * (x - p3[0]) + (p1[0] - p3[0]) * (z - p3[2])) / det;
		float I3 = 1.0f - I1 - I2;
		
		return I1 * p1[1] + I2 * p2[1] + I3 * p3[1];
	}
	
	//Close
	public void close() {
		this.generator.close();
		this.generator = null;
		this.gl = null;
		this.heightMap = null;
		this.texture = null;
		this.textureLoader.close();
		this.textureLoader = null;
		System.out.println("NoiseTerrain closed....");
	}
	
}
