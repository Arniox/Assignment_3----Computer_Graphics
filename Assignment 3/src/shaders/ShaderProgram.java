package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.jogamp.opengl.GL2;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private GL2 gl;
	
	//Constructor
	public ShaderProgram(GL2 gl, String vertexFile, String fragmentFile) {
		//Set main
		this.gl = gl;
		
		//Set shaders
		vertexShaderID = loadShader(vertexFile, GL2.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL2.GL_FRAGMENT_SHADER);
		
		//Set program ID
		programID = gl.glCreateProgram();
		//Attach
		gl.glAttachShader(programID, vertexShaderID);
		gl.glAttachShader(programID, fragmentShaderID);
		//Link program
		gl.glLinkProgram(programID);
		
		//Validate
		gl.glValidateProgram(programID);
		
	}
	
	//Start
	public void useProgram() {
		gl.glUseProgram(programID);
	}
	
	//Stop
	public void stop() {
		gl.glUseProgram(0);
	}
	
	//Cleanup
	public void cleanUp() {
		stop();
		gl.glDetachShader(programID, vertexShaderID);
		gl.glDetachShader(programID, fragmentShaderID);
		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);
		
		gl.glDeleteProgram(programID);
	}
	
	//Bind attributes
	protected abstract void bindAttributes(int attribte, String variable);
	
	protected void bindAttribute(int attribute, String variableName) {
		gl.glBindAttribLocation(programID, attribute, variableName);
	}
	
	//Read in shader
	private int loadShader(String file, int type) {
		//Read in shader source with try catch and string builder
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e) {
			System.out.println("[DEBUG] - Could not read shader file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Get shader ID
		int shaderID = gl.glCreateShader(type);
		//Set shader source
		gl.glShaderSource(shaderID, 1, new String[] {shaderSource.toString()}, null);
		//Compile
		gl.glCompileShader(shaderID);
		
		//Return
		System.out.println("[DEBUG] - Shader BUFFERED correctly");
		return shaderID;
	}
	
	
	//Get program ID
	public int getProgramID() {
		return this.programID;
	}
}
