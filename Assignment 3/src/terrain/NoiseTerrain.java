package terrain;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

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
	Texture grassTexture;
	Texture dirtTexture;
	
	//Constructor
	public NoiseTerrain(GL2 gl) {
		//Main variables
		this.gl = gl;
		
		//Heights
		this.heightMap = new float[SIZE+1][SIZE+1];
		this.generator = new HeightsGenerator();
		this.generateHeights();
		
		//Buffer images
		bufferTextures();
		
	}
	
	//Draw
	public void drawHeightMappedTerrain(int displayList) {
		//Push and pop
		gl.glPushMatrix();
		gl.glTranslated(-(SIZE/2), 0, -(SIZE/2));
		
		//Set terrain for height map
		for(int z=0; z<SIZE; z++) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for(int x=0; x<SIZE; x++) {
				gl.glNormal3fv(this.calculateNormal(x, z),0);
					gl.glVertex3f(x, heightMap[z][x], z);
					
				gl.glNormal3fv(this.calculateNormal(x, z+1),0);
					gl.glVertex3f(x, heightMap[z+1][x], z+1);
			}
			gl.glEnd();
		}
		
		//Pop
		gl.glPopMatrix();
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
	private void bufferTextures() {
		//Buffer in grass
		try {
			grassTexture = TextureIO.newTexture(new File("textures/Grass.png"), true);
			grassTexture.isUsingAutoMipmapGeneration();
			
			if(grassTexture != null) {
				System.out.println("[DEBUG] - Grass.png BUFFERED correctly");				
			}
		} catch (GLException | IOException e) {
			System.out.println("[DEBUG] - Grass.png FAILED to buffer correctly");
		}
		
		//Buffer in dirt
		try {
			dirtTexture = TextureIO.newTexture(new File("textures/Gravel.png"), true);
			dirtTexture.isUsingAutoMipmapGeneration();
			
			if(dirtTexture != null) {
				System.out.println("[DEBUG] - Gravel.png BUFFERED correctly");				
			}
		} catch (GLException | IOException e) {
			System.out.println("[DEBUG] - Gravel.png FAILED to buffer correctly");
		}
	}
	
}
