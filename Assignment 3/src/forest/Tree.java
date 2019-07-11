package forest;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import loader.ObjLoader;
import loader.RawModel;

public class Tree {
	//Main variables
	private GL2 gl;
	
	//Model
	private ObjLoader objLoader;
	private RawModel treeObject;
	
	//Constructor
	public Tree(GL2 gl) {
		this.gl = gl;
		
		objLoader = new ObjLoader(this.gl);
		
		//Load objects
		treeObject = objLoader.loadModel("objects/trees/tree1.obj", "trees/");
	}
	
	//Draw
	public void drawTree(float[] treePosition) {		
		//Push and pop for draw
		gl.glPushMatrix();
		gl.glTranslated(treePosition[0], treePosition[1], treePosition[2]);
		//gl.glRotated(-90, 1, 0, 0);
			//Draw all
			treeObject.drawObject(GL2.GL_POLYGON, 1f);
		//pop
		gl.glPopMatrix();
	}
}
