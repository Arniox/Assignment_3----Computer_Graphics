package textures;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureLoader {	
	//Constructor
	public TextureLoader() {
	}
	
	//Load texture
	public Texture loadTexture(String fileName) {
		System.out.println("[DEBUG] - Buffering texture: "+fileName+"...");
		
		Texture texture = null;
		try {
			texture = TextureIO.newTexture(new File(fileName), true);
			texture.isUsingAutoMipmapGeneration();
			
			return texture;
		} catch (GLException | IOException e) {
			return null;
		}
	}
}