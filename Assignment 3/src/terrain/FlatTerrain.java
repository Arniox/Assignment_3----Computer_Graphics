package terrain;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class FlatTerrain {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Terrain detail
	private int quadDetailX = 0;
	private int quadDetailZ = 0;
	
	//Constructor
	public FlatTerrain(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	//Draw
	public void drawTerrain(int terrainWidth, int terrainLength, int terrainDetail) {
		quadDetailX = terrainWidth/terrainDetail;
		quadDetailZ = terrainLength/terrainDetail;
		
		this.calculate(terrainWidth, terrainLength, terrainDetail);
	}
	
	//Calculate
	public void calculate(int terrainWidth, int terrainLength, int terrainDetail) {	
		
		//Set terrain		
		for(int z=terrainLength;z>-terrainLength;z--) {
			for(int x=-terrainWidth;x<terrainWidth;x++) {
				//Draw quad
				gl.glBegin(GL2.GL_QUADS);
				gl.glColor4fv(new float[]{0,1,0,1}, 0);
				
				//If first quad
				if(z==terrainLength && x==-terrainWidth) {
					gl.glVertex3f(x,0,z);
					gl.glVertex3f(x,0,(z-quadDetailZ));
					gl.glVertex3f((x-quadDetailX),0,(z-quadDetailZ));
					gl.glVertex3f((x-quadDetailX),0,z);
					gl.glVertex3f(x,0,z);
				}else {
					gl.glVertex3f(x,0,z);
					gl.glVertex3f(x,0,(z-quadDetailZ));
					gl.glVertex3f((x-quadDetailX),0,(z-quadDetailZ));
					gl.glVertex3f((x-quadDetailX),0,z);
					gl.glVertex3f(x,0,z);
				}

				gl.glEnd();
			}
		}
	}
}
