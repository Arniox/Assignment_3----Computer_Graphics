package skyBox;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import loader.ObjLoader;
import loader.RawModel;
import loader.TextureLoader;

public class SkyBox {
	//Maion variables
	private GL2 gl;
	
	//Texturing
	private TextureLoader textureLoader;
	private Texture[] textures;
	private Texture tex1;
	public static final String[] SKYBOX_TEXTURES = new String[]{"textures/day.png",
																"textures/clouds.png",
																"textures/night.png"};
	//Material
	private static final float SKY_DIFFUSE[] = {1,1,1,1};
	private static final float SKY_SPECULAR[] = {0,0,0,0};
	private static final float SKY_AMBIENT[] = {0.3f,0.3f,0.3f,1};
	private static final float SKY_SHININESS = 0;
	
	//Model
	private ObjLoader objLoader;
	private RawModel skyBoxDay;
	private RawModel skyBoxNight;
	private RawModel skyBoxCloud;
	
	//Constructor
	public SkyBox(GL2 gl) {
		this.gl = gl;

		objLoader = new ObjLoader(this.gl);
		
		//Load textures
		textureLoader = new TextureLoader();
		textures = new Texture[3];
		loadTextures();
		
		//Load objects
		skyBoxDay = objLoader.loadModel("objects/SkyBox.obj");
		skyBoxNight = objLoader.loadModel("objects/SkyBoxNight.obj");
		skyBoxCloud = objLoader.loadModel("objects/SkyBoxCloud.obj");
	}
	
	//Draw
	public void drawSkyBox(float[] characterPos) {		
		//Push and pop for draw
		gl.glPushMatrix();
		gl.glTranslated(characterPos[0], 50, characterPos[2]);
			//Load
			gl.glActiveTexture(GL2.GL_TEXTURE0);
			tex1 = textures[0];
			tex1.bind(gl);
			tex1.enable(gl);
			
			//Set texture parameter
			this.setTextureParameter();
			//Set material:
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, SKY_DIFFUSE, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, SKY_SPECULAR, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, SKY_AMBIENT, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, SKY_SHININESS);
	
			skyBoxDay.drawObject(GL2.GL_QUADS);
			
			//Disabled
			tex1.disable(gl);
		//Pop
		gl.glPopMatrix();
	}
	
	//Set texture parameter
	private void setTextureParameter() {
		//Set tex1
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		tex1.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
	}
	
	//Load textures
	private void loadTextures() {	
		System.out.println("\n[DEBUG] - Loading SkyBox Textures...");
		
		//Load terrain textures
		for(int i=0; i<SKYBOX_TEXTURES.length; i++) {
			textures[i] = textureLoader.loadTexture(SKYBOX_TEXTURES[i]);
			System.out.println("[DEBUG] - "+(i+1)+"/"+SKYBOX_TEXTURES.length+" loaded...");
			
			if(textures[i] != null) {
				System.out.println("[DEBUG] - "+SKYBOX_TEXTURES[i]+" BUFFERED correctly - "+textures[i].getWidth()+" x "+textures[i].getHeight());
			}else {
				System.out.println("[DEBUG] - "+SKYBOX_TEXTURES[i]+" FAILED to buffer correctly");	
			}
		}
	}
}
