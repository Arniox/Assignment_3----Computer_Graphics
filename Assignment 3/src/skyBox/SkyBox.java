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
	private Texture texture;
	public static final String[] SKYBOX_TEXTURES = new String[]{"textures/skybox/Day.png",
																"textures/skybox/CloudsNegated.png",
																"textures/skybox/Night.png"};
	//Material
	private static final float SKY_DIFFUSE[] = {1,1,1,1};
	private static final float SKY_SPECULAR[] = {0,0,0,0};
	private static final float SKY_AMBIENT[] = {0.3f,0.3f,0.3f,1};
	private static final float SKY_SHININESS = 0;
	private static final float NIGHT_DIFFUSE[] = {1,1,1,1};
	private static final float NIGHT_SPECULAR[] = {1,1,1,1};
	private static final float NIGHT_AMBIENT[] = {1,1,1,1};
	private static final float NIGHT_SHININESS = 0;
	
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
		skyBoxDay = objLoader.loadModel("objects/SkyBoxD.obj");
		skyBoxNight = objLoader.loadModel("objects/SkyBoxN.obj");
		skyBoxCloud = objLoader.loadModel("objects/SkyBoxC.obj");
	}
	
	//Draw
	public void drawSkyBox(float[] characterPos, boolean night) {
		//Push and pop for draw
		gl.glPushMatrix();
		gl.glTranslated(characterPos[0], 20, characterPos[2]);
			//Draw all	
			if(night) {
				this.drawNight();
			}else { this.drawDay(); }
			//this.drawClouds();
		//Pop
		gl.glPopMatrix();
	}
	
	//Draw Day skybox
	private void drawDay() {
		//Load
		this.bindTexture(0);		
		this.setTextureParameter();
		this.setMaterials(false);

		skyBoxDay.drawObject(GL2.GL_QUADS, 200);
		
		//Disabled
		texture.disable(gl);
	}
	
	//Draw Night skybox
	private void drawNight() {
		//Translate to fix texture being upside down
		gl.glRotated(180, 1, 0, 0);
		//Load
		this.bindTexture(2);		
		this.setTextureParameter();
		this.setMaterials(true);

		skyBoxNight.drawObject(GL2.GL_QUADS, 200);
		
		//Disabled
		texture.disable(gl);
		gl.glRotated(-180, 1, 0, 0);
	}
	
	//Draw Cloud skybox
	private void drawClouds() {
		//Translate to fix texture being upside down
		gl.glRotated(180, 1, 0, 0);
		//Load
		this.bindTexture(1);		
		this.setTextureParameter();
		this.setMaterials(false);

		skyBoxCloud.drawObject(GL2.GL_QUADS, 200);
		
		//Disabled
		texture.disable(gl);
		gl.glRotated(-180, 1, 0, 0);
	}
	
	//Bind texture
	private void bindTexture(int i) {
		//Load
		texture = textures[i];
		texture.bind(gl);
		texture.enable(gl);
	}
	
	//Set materials
	private void setMaterials(boolean night) {
		if(!night) {
			//Set materials for day and clouds texture
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, SKY_DIFFUSE, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, SKY_SPECULAR, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, SKY_AMBIENT, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, SKY_SHININESS);			
		}else {
			//Set materials for night
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, NIGHT_DIFFUSE, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, NIGHT_SPECULAR, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, NIGHT_AMBIENT, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, NIGHT_SHININESS);
		}
	}
	
	//Set texture parameter
	private void setTextureParameter() {
		//Set texture parameters
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
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
