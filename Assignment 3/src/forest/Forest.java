package forest;

import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import terrain.NoiseTerrain;

public class Forest {
	//Main variables
	private GL2 gl;
	private NoiseTerrain terrainObject;
	
	//Model
	private ArrayList<Tree> trees;
	private Tree currentTree;
	private ArrayList<float[]> treePosition;
	
	//Forest data
	private float forestDensitity;
	private float forestSize;
	private int countOfTrees = 0;
	
	//Constructor
	public Forest(GL2 gl, NoiseTerrain terrainObject) {
		this.gl = gl;
		this.terrainObject = terrainObject;
		
		//Intialize the tree array and tree position array
		trees = new ArrayList<Tree>();
		treePosition = new ArrayList<float[]>();
		//Run set up
		setUpForest();
	}
	
	//Set up forest
	private void setUpForest() {
		this.forestSize = (float)Math.random()*(this.terrainObject.getSize()/2); //Size is between 0 and the size of the terrain/2 so as to not get too big
		this.forestDensitity = (float)Math.random()*(forestSize*800); //Forest density between 1 and forestSize size * 800. Forest size is divided by this to get number of trees
		
		//Set count of trees
		countOfTrees = (int)forestDensitity/(int)forestSize;
		currentTree = new Tree(gl);
		
		int currentCountOfTrees = 0;
		for(int i=0; i<countOfTrees; i++) {
			trees.add(currentTree);
			
			//Set a random position into the array
			float randomX = (float)Math.random()*(forestSize*2f)-forestSize; //From -forestSize -> forestSize
			float randomZ = (float)Math.random()*(forestSize*2f)-forestSize; //From -forestSize -> forestSize
			float heightAtRandom = terrainObject.getHeightBellow(randomX, randomZ)+1.5f; //Lift tree up slightly to help with clipping
			//Don't place in water
			if(!(heightAtRandom<0)) {
				treePosition.add(new float[]{randomX,heightAtRandom,randomZ});
				currentCountOfTrees++;
			}
		}
		
		//Set the new count of trees
		countOfTrees = currentCountOfTrees;
	}
	
	//Draw
	public void drawForest(int displayList, float[] playerPosition, float skyBoxSize) {
		//Display list the whole entire forest and all objects inside
		//gl.glNewList(displayList, GL2.GL_COMPILE);
		//Disable face cull to be able to see the under side of trees and so on
		gl.glDisable(GL.GL_CULL_FACE);
		
		//For each tree, place randomly
		for(int i=0; i<countOfTrees; i++) {
			//Use position from already generated coords
			//Only draw trees if they are within player range
			if(!(getDistance(playerPosition, treePosition.get(i))>(skyBoxSize*1.4))) {
				trees.get(i).drawTree(treePosition.get(i));
			}
		}
		
		//Re-enable cull face for optimization
		gl.glEnable(GL.GL_CULL_FACE);
		//End list
		//gl.glEndList();
	}
	
	//Get distance
	private float getDistance(float[] input1, float[] input2) {
		//Make values all absolute
		float[] point1 = new float[3];
		float[] point2 = new float[3];
		//Point 1
		point1[0] = Math.abs(input1[0]);
		point1[1] = Math.abs(input1[2]);
		point1[2] = Math.abs(input1[2]);
		//Point 2
		point2[0] = Math.abs(input2[0]);
		point2[1] = Math.abs(input2[2]);
		point2[2] = Math.abs(input2[2]);
		
		return (float)Math.sqrt(Math.pow((point2[0]-point1[0]), 2)+Math.pow((point2[2]-point1[2]), 2));
	}
	
}
