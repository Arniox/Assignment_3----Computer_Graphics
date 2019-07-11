package terrain;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import loader.TextureLoader;

public class WaterLevel {
	//Main variables
	private GL2 gl;
	
	//Building variables
	private int size;
	private int waterHeight;
	
	//Textures
	private TextureLoader textureLoader;
	private Texture texture;
	
	//Material
	private static final float WATER_DIFFUSE[] = {0.9f,0.9f,1,0.65f};
	private static final float WATER_SPECULAR[] = {0,0,1,0.65f};
	private static final float WATER_AMBIENT[] = {0,0,1,0.65f};
	private static final float WATER_SHININESS = 80;
	
	//Constructor
	public WaterLevel(GL2 gl, int size, int waterHeight) {
		//Set main variables
		this.gl = gl;
		
		this.size = size;
		this.waterHeight = waterHeight;
		
		//Load textures
		this.textureLoader = new TextureLoader();
		loadTextures();
	}
	
	//Draw
	public void drawWater() {
		//Disable culling face so that the underside of water still displays
		gl.glDisable(GL.GL_CULL_FACE);
		gl.glShadeModel(GL2.GL_SMOOTH);
		
		//Push and pop
		gl.glPushMatrix();
		
		//Load
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		texture.bind(gl);
		texture.enable(gl);
		
		//Set texture parameter
		this.setTextureParameter();
		//Set material:
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, WATER_DIFFUSE, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, WATER_SPECULAR, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, WATER_AMBIENT, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, WATER_SHININESS);
		
		gl.glBegin(GL2.GL_QUADS);
			gl.glNormal3f(0,1,0);
			gl.glTexCoord2f(size/2, size/2);
			gl.glVertex3f(size/2, waterHeight, size/2);
			gl.glNormal3f(0,1,0);
			gl.glTexCoord2f(size/2, -size/2);
			gl.glVertex3f(size/2, waterHeight, -size/2);
			gl.glNormal3f(0,1,0);
			gl.glTexCoord2f(-size/2, -size/2);
			gl.glVertex3f(-size/2, waterHeight, -size/2);
			gl.glNormal3f(0,1,0);
			gl.glTexCoord2f(-size/2, size/2);
			gl.glVertex3f(-size/2, waterHeight, size/2);
		gl.glEnd();

		//Disable
		texture.disable(gl);
		
		//Pop
		gl.glPopMatrix();
		
		//Re-enable culling
		gl.glEnable(GL.GL_CULL_FACE);
	}
	
	//Load textures
	private void loadTextures() {
		System.out.println("\n[DEBUG] - Loading Water Textures...");
		
		//Load terrain textures
			texture = textureLoader.loadTexture("textures/water/water.jpg");
			
			if(texture != null) {
				System.out.println("[DEBUG] - textures/water/water.jpg BUFFERED correctly - "+texture.getWidth()+" x "+texture.getHeight());
			}else {
				System.out.println("[DEBUG] - textures/water/water.jpg FAILED to buffer correctly");
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
	
	//Close
	public void close() {
		this.gl = null;
		this.texture = null;
		this.textureLoader.close();
		this.textureLoader = null;
		System.out.println("WaterLevel closed....");
	}
	
	//Get water height
	public int getWaterheight() {
		return this.waterHeight;
	}
	
}
