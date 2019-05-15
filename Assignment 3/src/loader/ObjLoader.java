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
	private String smoothingType;
	//Array list data
	private ArrayList<float[]> vData = new ArrayList<float[]>();
	private ArrayList<float[]> vtData = new ArrayList<float[]>();
	private ArrayList<float[]> vnData = new ArrayList<float[]>();
	private ArrayList<int[][]> faceData = new ArrayList<int[][]>();
	//Array data
	private float[] vertexes;
	private float[] textureCoords;
	private float[] normals;
	private int[] faces;
	
	//Constructor
	public ObjLoader(GL2 gl) {
	}
	
	//Load object
	public RawModel loadModel(String fileName) {
		System.out.println("\n[DEBUG] - Buffering Object: "+fileName+"...");
		
		//Buffer and store object data
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			int linesScanned = 0;
			
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					System.out.print("[DEBUG] - "+line+" | ");
					linesScanned++;
				}else if(line.startsWith(" ")) {//Ignore whitespace
				}else if(line.startsWith("o ")) { //Read model name
					modelName = line.substring(2);
					linesScanned++;
				}else if(line.startsWith("v ")) { //Read vertex data
					vData.add(ProcessData(line));
					linesScanned++;
				}else if(line.startsWith("vt ")) { //Read texture coords
					vtData.add(ProcessData(line));
					linesScanned++;
				}else if(line.startsWith("vn ")) { //Read normals
					vnData.add(ProcessData(line));
					linesScanned++;
				}else if(line.startsWith("s ")) { //Read smoothing type
					smoothingType = line.substring(2);
				}else if(line.startsWith("f ")) { //Read face data
					faceData.add(ProcessFData(line));
					linesScanned++;
				}
			}
			
			System.out.println("\n[DEBUG] - Finished Scanning Object: "+modelName+". Lines: "+linesScanned+". Data: "+(vData.size()+vtData.size()+vnData.size()+faceData.size()));
			
			return new RawModel(gl, smoothingType, vData, vtData, vnData, faceData);
			
 		}catch(IOException e) {
			System.out.println("[DEBUG] - FAILED to buffer: "+fileName);
			
			return null;
		}
	}
	
	//Process all data into arrays
	private void processAndSet() {
		vertexes = new float[vData.size()*3];
		textureCoords = new float[vtData.size()*2];
		normals = new float[vnData.size()*3];
		faces = new int[faceData.size()*faceData.get(0).length*faceData.get(0)[0].length];
		
		
	}
	
	//Process data into string array and return float parsed data
	private float[] ProcessData(String read) {
		final String s[] = read.split("\\s+");
		return (ProcessFloatData(s));
	}
	
	//Process face data into string array and return float parsed data
	private int[][] ProcessFData(String read) {
		final String s[] = read.split("\\s+");
		final String j[] = new String[s.length-1];
		
		final int k[][] = new int[j.length][(s[1].length())-(s[1].length()/2)];
		//Remove "f"
		for(int i=0; i<j.length; i++) {
			if(s[i] != "f") {
				j[i] = s[i+1];
			}
			//Remove "/"
			int zCount = 0;
			for(int z=0; z<j[i].length(); z++) {
				String g = "";
				//Find any number and concatenate into g
				int forwards = 0;
				while(getChar(j[i], z+forwards) != '/') {
					g += getChar(j[i], z+forwards)+"";
					
					//Add 1 to zCount to iterate k
					if(forwards==0) {
						zCount++;
					}
					forwards++;
				}
				
				z+=forwards;
				k[i][zCount-1] = Integer.parseInt(g);
			}
		}
		
		return k;
	}
	
	//Get char. if exception, return char '/' to break the loop (this is for end of face vertex)
	private char getChar(String o, int pos) {
		try {
			return o.charAt(pos);
		}catch(Exception e) {
			return '/';
		}
	}
	
	//Process string data into a float array
	private float[] ProcessFloatData(String sData[]) {
		float data[] = new float[sData.length-1];
		for(int loop=0; loop<data.length; loop++) {
			data[loop] = Float.parseFloat(sData[loop+1]);
		}
		
		return data;
	}
}
