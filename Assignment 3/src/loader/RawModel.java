package loader;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class RawModel {
	//Main
	private GL2 gl;
	
	//Settings
	String smoothingType;
	ArrayList<float[]> vData = new ArrayList<float[]>();
	ArrayList<float[]> vtData = new ArrayList<float[]>();
	ArrayList<float[]> vnData = new ArrayList<float[]>();
	ArrayList<int[][]> faceData = new ArrayList<int[][]>();
	
	//Constructor
	public RawModel(GL2 gl, String smoothingType, ArrayList<float[]> inVData, ArrayList<float[]> inVtData, ArrayList<float[]> inVnData, ArrayList<int[][]> inFaceData) {
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
	public void drawObject() {
		//Set shading model
		if(smoothingType == "1") {
			gl.glShadeModel(GL2.GL_SMOOTH);
		}else if(smoothingType == "0") {
			gl.glShadeModel(GL2.GL_FLAT);
		}
		
		//Set data
		for(int[][] surface : faceData) {
			//gl.glVertex3f(, , );
		}
		
	}
	
	//List all dat
	public void listAll() {
		System.out.println("Smoothing type: "+smoothingType);
		System.out.println("V Data Count: "+vData.size());
		System.out.println("Vt Data Count: "+vtData.size());
		System.out.println("Vn Data Count: "+vnData.size());
		System.out.println("Face Data Count: "+faceData.size());
	}
	
}
