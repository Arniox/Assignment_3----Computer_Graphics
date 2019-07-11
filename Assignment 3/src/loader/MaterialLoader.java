package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MaterialLoader {
	//Data
	private float shininessValue;
	private ArrayList<float[]> ambient = new ArrayList<float[]>();
	private ArrayList<float[]> diffuse = new ArrayList<float[]>();
	private ArrayList<float[]> specular = new ArrayList<float[]>();
	//Material
	private ArrayList<Material> materials;
	
	//Constructor
	public MaterialLoader() {
		//Set up new material array list
		materials = new ArrayList<Material>();
	}
	
	//Load Material
	public void loadMaterial(String fileName) {
		System.out.println("[DEBUG] - Buffering Material: "+fileName+"...");
		//Clean up
		ambient = new ArrayList<float[]>();
		diffuse = new ArrayList<float[]>();
		specular = new ArrayList<float[]>();
		
		//Count materials
		int materialCount = -1;
		
		//Buffer and store object data
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			
			//While
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					if(line.startsWith("# Blender")) {
						System.out.println("[DEBUG] - "+line);
					}
				}else if(line.startsWith(" ")) { //Ignore whitespace
				}else if(line.startsWith("newmtl ")) { //Get material name
					materials.add(new Material(processName(line)));
					materialCount++;
				}else if(line.startsWith("Ns ")) { //Get material shininess
					shininessValue = processShininess(line);
					materials.get(materialCount).setShininessValue(shininessValue);
				}else if(line.startsWith("Ka ")) {
					ambient.add(processData(line));
					materials.get(materialCount).setAmbient(ambient.get(materialCount));
				}else if(line.startsWith("Kd ")) {
					diffuse.add(processData(line));
					materials.get(materialCount).setDiffuse(diffuse.get(materialCount));
				}else if(line.startsWith("Ks ")) {
					specular.add(processData(line));
					materials.get(materialCount).setSpecular(specular.get(materialCount));
				}
			}
			
			System.out.println("[DEBUG] - "+fileName+" has buffered CORRECTLY");
			br.close();
		}catch(IOException e) {
			System.out.println("[DEBUG] - FAILED to buffer: "+fileName);
		}
	}
	
	//Process Name
	public String processName(String line) {
		String s[] = line.split(" ");
		return s[1];
	}
	
	//Process Shininess float
	public float processShininess(String line) {
		String s[] = line.split(" ");
		return Float.parseFloat(s[1]);
	}
	
	//Process data into string array and return float parsed data
	public float[] processData(String line) {
		final String s[] = line.split("\\s+");
		return (processFloatData(s));
	}
	
	//Process string data into a float array
	public float[] processFloatData(String sData[]) {
		float data[] = new float[sData.length-1];
		for(int loop=0; loop<data.length; loop++) {
			data[loop] = Float.parseFloat(sData[loop+1]);
		}
		
		return data;
	}
	
	//Close
	public void close() {
		this.ambient = null;
		this.diffuse = null;
		for(Material m : this.materials) {
			m.close();
		}
		this.materials = null;
		this.specular = null;
		System.out.println("MaterialLoader closed....");
	}
	
	//Get materials
	public ArrayList<Material> getMaterials(){
		return this.materials;
	}
}
