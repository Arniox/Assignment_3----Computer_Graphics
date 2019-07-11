package loader;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class RawModel {
	//Main
	private GL2 gl;
	
	//Loader
	MaterialLoader materialLoader;
	ArrayList<Material> materials;
	
	//Settings
	int smoothingType;
	//Materials
	ArrayList<String> materialNames;
	String materialFile;
	ArrayList<Integer> materialPosition;
	//v Data
	ArrayList<float[]> vData;
	ArrayList<float[]> vtData;
	ArrayList<float[]> vnData;
	ArrayList<int[][]> faceData;
	
	//Constructor
	public RawModel(GL2 gl, int smoothingType, String fileSubExtension,
					String materialFile, ArrayList<String> materials, ArrayList<Integer> matPos,
					ArrayList<float[]> inVData, ArrayList<float[]> inVtData, ArrayList<float[]> inVnData, 
					ArrayList<int[][]> inFaceData) {
		//Set main
		this.gl = gl;
		this.materialLoader = new MaterialLoader();
		//Create new RawModel object
		this.smoothingType = smoothingType;
		this.materialNames = materials;
		this.materialFile = materialFile;
		this.materialPosition = matPos;
		this.vData = inVData;
		this.vtData = inVtData;
		this.vnData = inVnData;
		this.faceData = inFaceData;
		
		//List all data:
		listAll();
		//Load materials
		loadMaterials(fileSubExtension);
	}
	
	//Load material
	private void loadMaterials(String subFileExtension) {
		if(materialFile != null) {
			if(!materialNames.get(0).equals("None")) {
				//Load all materials
				materialLoader.loadMaterial("objects/"+subFileExtension+materialFile);
				//Get the material objects
				materials = materialLoader.getMaterials();
			}else {
				//Otherwise clean up ram
				materialLoader = null;
				materials = null;
				materialNames = null;
				materialFile = null;
				materialPosition = null;				
			}
		}
	}
	
	//Draw
	public void drawObject(int objectType, float sizeMultiplier) {
		//Set shading model
		if(smoothingType == 1) {
			gl.glShadeModel(GL2.GL_SMOOTH);
		}else if(smoothingType == 0) {
			gl.glShadeModel(GL2.GL_FLAT);
		}
		
		int faceCount = 0;
		int materialCount = -1;
		//Set data
		for(int[][] surface : faceData) {
			if(materials != null) {
				for(int matPos : materialPosition) {
					if(faceCount==matPos) {
						materialCount++;
					}
				}
				//Get values
				float[] materialDiffuse = materials.get(materialCount).getDiffuse();
				float[] materialSpecular = materials.get(materialCount).getSpecular();
				float[] materialAmbient = materials.get(materialCount).getAmbient();
				float shininess = materials.get(materialCount).getShininessValue();
				
				//Set values
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, materialDiffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, materialSpecular, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, materialAmbient, 0);
				gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);
			}
			
			
			gl.glBegin(objectType);
			for(int[] point : surface) {
				//Set normal, vertex and textureCoord. -1 from index because obj files start from 1 instead of 0
				gl.glNormal3f(vnData.get(point[2]-1)[0], vnData.get(point[2]-1)[1], vnData.get(point[2]-1)[2]);
				if(!vtData.isEmpty()) {
					gl.glTexCoord2f(vtData.get(point[1]-1)[0], vtData.get(point[1]-1)[1]);
				}
				gl.glVertex3f(vData.get(point[0]-1)[0]*sizeMultiplier, vData.get(point[0]-1)[1]*sizeMultiplier, vData.get(point[0]-1)[2]*sizeMultiplier);
			}
			gl.glEnd();
			
			faceCount++;
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
	
	//Close
	public void close() {
		this.faceData = null;
		this.gl = null;
		this.materialFile = null;
		this.materialLoader.close();
		this.materialLoader = null;
		this.materialNames = null;
		for(Material m : this.materials) {
			m.close();
		}
		this.materials = null;
		this.vData = null;
		this.vnData = null;
		this.vtData = null;
		System.out.println("RawModel closed....");
	}
	
}
