package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

//Found info at http://forum.jogamp.org/Loading-and-drawing-obj-models-td2708428.html
public class ObjLoader {
	//Main
	private GL2 gl;
	
	//data
	private String modelName;
	private int smoothingType;
	private String materialFile;
	private ArrayList<String> materials;
	private ArrayList<Integer> materialPosition;
	//Array list data
	private ArrayList<float[]> vData = new ArrayList<float[]>();
	private ArrayList<float[]> vtData = new ArrayList<float[]>();
	private ArrayList<float[]> vnData = new ArrayList<float[]>();
	private ArrayList<int[][]> faceData = new ArrayList<int[][]>();
	
	//Constructor
	public ObjLoader(GL2 gl) {
		this.gl = gl;
	}
	
	//Load object
	public RawModel loadModel(String fileName, String subFileExtension) {
		System.out.println("\n[DEBUG] - Buffering Object: "+fileName+"...");
		//Clean up
		vData = new ArrayList<float[]>();
		vtData = new ArrayList<float[]>();
		vnData = new ArrayList<float[]>();
		faceData = new ArrayList<int[][]>();
		materials = new ArrayList<String>();
		materialPosition = new ArrayList<Integer>();
		
		
		//Buffer and store object data
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			int linesScanned = 0;
			int matPos = 0;
			
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					if(line.startsWith("# Blender")) {
						System.out.print("[DEBUG] - "+line+" | ");	
						linesScanned++;					
					}
				}else if(line.startsWith(" ")) {//Ignore whitespace
				}else if(line.startsWith("mtllib ")) {	//Read material file name
					materialFile = processMaterial(line);
				}else if(line.startsWith("o ")) { //Read model name
					modelName = line.substring(2);
					linesScanned++;
				}else if(line.startsWith("v ")) { //Read vertex data
					vData.add(processData(line));
					linesScanned++;
				}else if(line.startsWith("vt ")) { //Read texture coords
					vtData.add(processData(line));
					linesScanned++;
				}else if(line.startsWith("vn ")) { //Read normals
					vnData.add(processData(line));
					linesScanned++;
				}else if(line.startsWith("s ")) { //Read smoothing type
					if(line.startsWith("s 1")) {
						smoothingType = Integer.parseInt(line.substring(2));						
					}else {
						smoothingType = 0;
					}
				}else if(line.startsWith("usemtl ")){ //Read the material
					materials.add(processMaterial(line));
					materialPosition.add(matPos);
				}else if(line.startsWith("f ")) { //Read face data
					faceData.add(processFData(line));
					linesScanned++;
					matPos++;
				}
			}
			
			System.out.println("\n[DEBUG] - Finished Scanning Object: "+modelName+". Lines: "+linesScanned+". Data: "+(vData.size()+vtData.size()+vnData.size()+faceData.size()));
			
			br.close();
			
			return new RawModel(gl, smoothingType, subFileExtension, materialFile, materials, materialPosition, vData, vtData, vnData, faceData);
			
 		}catch(IOException e) {
			System.out.println("[DEBUG] - FAILED to buffer: "+fileName);
			
			return null;
		}
	}
	
	//Process material
	private String processMaterial(String line) {
		String s[] = line.split(" ");
		return s[1];
	}
	
	//Process data into string array and return float parsed data
	private float[] processData(String read) {
		final String s[] = read.split("\\s+");
		return (processFloatData(s));
	}
	
	//Process string data into a float array
	private float[] processFloatData(String sData[]) {
		float data[] = new float[sData.length-1];
		for(int loop=0; loop<data.length; loop++) {
			data[loop] = Float.parseFloat(sData[loop+1]);
		}
		
		return data;
	}
	
	//Process face data into string array and return float parsed data
	private int[][] processFData(String read) {
		//Two string arrays: s is array for each element including the 'f'. j is for each element without 'f'
		final String s[] = read.split("\\s+");
		final String j[] = new String[s.length-1];
		
		//Float double array with z length of a faces vertexes. A cube would have 4 vertexes
		//x length is the string length - the number of dashes 1/1/1. Total length is 5-(int)5/2
		final int k[][] = new int[j.length][(s[1].length())-(s[1].length()/2)];
		//Remove "f"
		for(int i=0; i<j.length; i++) {
			if(s[i] != "f") {
				j[i] = s[i+1];
				
				String numString[] = j[i].split("/");
				int[] nums = new int[numString.length];
				for(int pop = 0; pop<numString.length; pop++) {
					if(numString[pop].isEmpty()) {
						numString[pop] = "0";
					}
					nums[pop] = Integer.parseInt(numString[pop]);
				}
				
				k[i] = nums;
			}
			
		}
		
		return k;
	}
	
	//Close
	public void close() {
		this.faceData = null;
		this.gl = null;
		this.materialPosition = null;
		this.materials = null;
		this.vData = null;
		this.vtData = null;
		this.vnData = null;
		System.out.println("ObjLoader closed....");
	}
}
