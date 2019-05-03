package terrain;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class FlatTerrain {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Terrain detail
	private float quadDetailX = 0;
	private float quadDetailZ = 0;
	
	//Constructor
	public FlatTerrain(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	//Draw
	public void drawTerrain(float terrainWidth, float terrainLength, float terrainDetail) {
		quadDetailX = terrainWidth/terrainDetail;
		quadDetailZ = terrainLength/terrainDetail;
		
		this.calculate(terrainWidth, terrainLength, terrainDetail);
	}
	
	//Calculate
	public void calculate(float terrainWidth, float terrainLength, float terrainDetail) {	
		int zCoordinateCount = (int)terrainLength;
		int xCoordinateCount = (int)terrainWidth;
		
		//Set terrain		
		for(int z=zCoordinateCount;z>-zCoordinateCount;z--) {
			for(int x=-xCoordinateCount;x<xCoordinateCount;x++) {
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
