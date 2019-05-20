package loader;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class RawModel {
	//Main
	private GL2 gl;
	
	//Settings
	int smoothingType;
	ArrayList<float[]> vData;
	ArrayList<float[]> vtData;
	ArrayList<float[]> vnData;
	ArrayList<int[][]> faceData;
	
	//Constructor
	public RawModel(GL2 gl, int smoothingType, ArrayList<float[]> inVData, ArrayList<float[]> inVtData, ArrayList<float[]> inVnData, ArrayList<int[][]> inFaceData) {
		//Set main
		this.gl = gl;
		//Create new RawModel object
		this.smoothingType = smoothingType;
		this.vData = inVData;
		this.vtData = inVtData;
		this.vnData = inVnData;
		this.faceData = inFaceData;
		
		//List all data:
		listAll();
	}
	
	//Draw
	public void drawObject(int objectType, float sizeMultiplier) {
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
				gl.glNormal3f(vnData.get(point[2]-1)[0], vnData.get(point[2]-1)[1], vnData.get(point[2]-1)[2]);
				gl.glTexCoord2f(vtData.get(point[1]-1)[0], vtData.get(point[1]-1)[1]);
				gl.glVertex3f(vData.get(point[0]-1)[0]*sizeMultiplier, vData.get(point[0]-1)[1]*sizeMultiplier, vData.get(point[0]-1)[2]*sizeMultiplier);
			}
			gl.glEnd();
		}
		
	}
	
	//List all dat
	public void listAll() {
		System.out.println("        - Smoothing type: "+smoothingType);
		System.out.println("        - Vertexes: "+vData.size());
		System.out.println("        - Texture translations: "+vtData.size());
		System.out.println("        - Normals: "+vnData.size());
		System.out.println("        - Faces: "+faceData.size());
	}
	
}
