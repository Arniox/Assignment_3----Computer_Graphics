package shaders;

import com.jogamp.opengl.GL2;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	public StaticShader(GL2 gl) {
		super(gl, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		//super.bindAttribute(0, "position");
		
	}
	
}
