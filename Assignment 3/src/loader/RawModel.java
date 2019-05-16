package loader;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class RawModel {
	//Main
	private GL2 gl;
	
	//Settings
	int smoothingType;
	float[] vData;
	float[] vtData;
	float[] vnData;
	ArrayList<int[][]> faceData;
	
	//Constructor
	public RawModel(GL2 gl, int smoothingType, float[] inVData, float[] inVtData, float[] inVnData, ArrayList<int[][]> inFaceData) {
		//Set main
		this.gl = gl;
		//Create new RawModel object
		this.smoothingType = smoothingType;
		this.vData = inVData;
		this.vtData = inVtData;
		this.vnData = inVnData;
		this.faceData = inFaceData;
	}
	
	//Draw
	public void drawObject(int objectType) {
		//Set shading model
		if(smoothingType == 1) {
			gl.glShadeModel(GL2.GL_SMOOTH);
		}else if(smoothingType == 0) {
			gl.glShadeModel(GL2.GL_FLAT);
		}
		
		//Set data
		for(int[][] surface : faceData) {
			gl.glBegin(objectType);
			for(int[] point : surface) {
				//Set normal, vertex and textureCoord. -1 from index because obj files start from 1 instead of 0
				gl.glNormal3f(vnData[point[0]-1], vnData[point[1]-1], vnData[point[2]-1]);
				gl.glVertex3f(vData[point[0]-1]*100, vData[point[1]-1]*100, vData[point[2]-1]*100);
			}
			gl.glEnd();
		}
		
	}
	
	//List all dat
	public void listAll() {
		System.out.println("Smoothing type: "+smoothingType);
		System.out.println("V Data Count: "+vData.length);
		System.out.println("Vt Data Count: "+vtData.length);
		System.out.println("Vn Data Count: "+vnData.length);
		System.out.println("Face Data Count: "+faceData.size());
	}
	
}
