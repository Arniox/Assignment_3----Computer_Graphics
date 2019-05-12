package onUnusedClass;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

//https://www.redblobgames.com/maps/terrain-from-noise/

public class FlatTerrain {
	//Main variables
	private GL2 gl;
	
	//Terrain detail
	int terrainDetail;
	float zCoordCount;
	float xCoordCount;
	float zQuadDetail;
	float xQuadDetail;
	
	//Terrain Detail for heightMaping
	int imgWidth;
	int imgHeight;
	
	//Terrain generation types
	ArrayList<float[]> yList; //Random
	double[][] heightMap = null;
	
	//Constructor
	public FlatTerrain(GL2 gl, float terrainWidth, float terrainLength, int terrainDetail, float terrainStartDepth) {
		this.gl = gl;
		
		//If detail is odd, convert
		if(terrainDetail%2!=0 && terrainDetail!=1) {
			terrainDetail+=1;
		}
		
		this.terrainDetail = terrainDetail;
		//Calculate size and detail of chunk
		this.zCoordCount = terrainLength/2;
		this.xCoordCount = terrainWidth/2;
		this.zQuadDetail = zCoordCount/terrainDetail;
		this.xQuadDetail = xCoordCount/terrainDetail;

		this.calculate(terrainStartDepth);		
	}
	
	//Draw
	public void drawTerrain() {
		//Terrain width is the width from one end to the other so from 0, 0, one side extends out by Terrain width/2
		//Terrain length is the length from one end to the other so from 0, 0, one side etends out by Terrain length/2
		//Terrain detail is roughly how detailed the terrain is. At terrain width of 5 and a terrain length of 5 and a detail
		//value of 5 means the terrain will be made of 5 x 5 quads
		int yCount = 0;
		
		//Set terrain for random
		for(float z=zCoordCount; z>-zCoordCount; z-=zQuadDetail) {
			for(float x=-xCoordCount; x<xCoordCount; x+=xQuadDetail) {
				
				//Draw Quad
				gl.glBegin(GL2.GL_QUADS);
				gl.glColor4fv(new float[]{0,0,0,1}, 0);
				
				gl.glVertex3f((x+xQuadDetail),yList.get(yCount)[0],z);
				gl.glVertex3f((x+xQuadDetail),yList.get(yCount)[1],(z-zQuadDetail));
				gl.glVertex3f(x,yList.get(yCount)[2],(z-zQuadDetail));
				gl.glVertex3f(x,yList.get(yCount)[3],z);
				
				gl.glEnd();
				
				yCount++;
			}
		}
		
	}
	
	//Calculate
	public void calculate(float maxMin) {
		int quadCount = 0;
		
		//Set up array list for y values
		yList = new ArrayList<float[]>();
		float max = maxMin;
		float min = -maxMin;
		float heightRange = (max-min);
		for(int z=0; z<terrainDetail*2; z++) {
			for(int x=0; x<terrainDetail*2; x++) {
				//If first quad
				if(z==0 && x==0) {
					yList.add(new float[]{(float)(Math.random()*heightRange)+min,
										  (float)(Math.random()*heightRange)+min,
										  (float)(Math.random()*heightRange)+min,
										  (float)(Math.random()*heightRange)+min});
				}
				//If first row
				else if(z==0 && x!=0) {
					yList.add(new float[]{(float)(Math.random()*heightRange)+min,
										  (float)(Math.random()*heightRange)+min,
										  yList.get(x-1)[1],
										  yList.get(x-1)[0]});
				}
				//If edge quad
				else if(x==0 && z!=0) {
					yList.add(new float[]{yList.get(quadCount-(terrainDetail*2))[1],
										  (float)(Math.random()*heightRange)+min,
										  (float)(Math.random()*heightRange)+min,
										  yList.get(quadCount-(terrainDetail*2))[2]});
				}
				//Everything else
				else {
					yList.add(new float[]{yList.get(quadCount-(terrainDetail*2))[1],
										  (float)(Math.random()*heightRange)+min,
										  yList.get(quadCount-1)[1],
										  yList.get(quadCount-(terrainDetail*2))[2]});
				}
				
				quadCount++;
			}
		}
	}
	
	
}
