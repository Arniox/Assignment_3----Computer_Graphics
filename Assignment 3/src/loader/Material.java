package loader;

public class Material {
	//Data
	private String materialName;
	private float shininessValue;
	private float[] ambient = new float[4];
	private float[] diffuse = new float[4];
	private float[] specular = new float[4];
	
	//Constructor
	public Material(String matName) {
		//Set up variables
		this.materialName = matName;
	}

	//Close
	public void close() {
		this.ambient = null;
		this.diffuse = null;
		this.specular = null;
		System.out.println("Material closed....");
	}
	
	//Getter
	public String getMaterialName() {
		return materialName;
	}
	public float getShininessValue() {
		return shininessValue;
	}
	public float[] getAmbient() {
		return ambient;
	}
	public float[] getDiffuse() {
		return diffuse;
	}
	public float[] getSpecular() {
		return specular;
	}

	//Setters
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public void setShininessValue(float shininessValue) {
		this.shininessValue = shininessValue;
	}
	public void setAmbient(float[] ambient) {
		this.ambient[0] = ambient[0];
		this.ambient[1] = ambient[1];
		this.ambient[2] = ambient[2];
		this.ambient[3] = 1.0f;
	}
	public void setDiffuse(float[] diffuse) {
		this.diffuse[0] = diffuse[0];
		this.diffuse[1] = diffuse[1];
		this.diffuse[2] = diffuse[2];
		this.diffuse[3] = 1.0f;
	}
	public void setSpecular(float[] specular) {
		this.specular[0] = specular[0];
		this.specular[1] = specular[1];
		this.specular[2] = specular[2];
		this.specular[3] = 1.0f;
	}	
}
