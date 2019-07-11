package fog;

import com.jogamp.opengl.GL2;

public class Fog {
	//main
	private GL2 gl;
	
	//Variables
	private float[] normalColour;
	private float[] underWaterColour;
	
	//Constructor
	public Fog(GL2 gl) {
		//Set main
		this.gl = gl;
		
		//Turn on fog
		gl.glEnable(GL2.GL_FOG);
		
		//Set colours up
		normalColour = new float[]{0.5f,0.5f,0.5f,1f};
		underWaterColour = new float[]{0,0.2f,0.8f,1f};
		
		//Initiate
		this.initiateFog();
	}
	
	//Run on display
	public void drawFog(boolean underwater) {
		//Set fog colour
		if(!underwater) {
			gl.glFogfv(GL2.GL_FOG_COLOR, normalColour, 0);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.006f);
		}else {
			gl.glFogfv(GL2.GL_FOG_COLOR, underWaterColour, 0);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.07f);
		}
		
	}
	
	//Initiate the base variables of the fog
	public void initiateFog() {
		//Set fog mode
		gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_EXP2);
		//Set fog start and end
		gl.glFogf(GL2.GL_FOG_START, 500.0f);
		gl.glFogf(GL2.GL_FOG_END, 510.0f);
	}
}
