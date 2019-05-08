package terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class HeightMappingTerrain {
	//Main variables
	private GL2 gl;
	private GLUT glut;
	
	//Terrain Detail for heightMaping
	int imgWidth;
	int imgHeight;

	//Terrain generation types
	float[][] heightMap = null;
	
	//Textures
	Texture grassTexture;
	Texture gravelTexture;
	
	//Constructor
	public HeightMappingTerrain(GL2 gl, GLUT glut) {
		//Main variables
		this.gl = gl;
		this.glut = glut;
		
		//Set up height map
		this.getHeightMap();
		
		//Set up textures
		this.bufferImages();
		
//		grassTexture.enable(this.gl);
//		gravelTexture.enable(this.gl);
	}
	
	//Draw
	public void drawHeightMappedTerrain(int displayList) {
		//gl.glNewList(displayList, GL2.GL_COMPILE);
		
		//Push and pop
		gl.glPushMatrix();
		
			gl.glTranslated(-(imgWidth/2), 0, -(imgHeight/2));
			//Set terrain for height map
			for(int z=0; z<imgHeight-1; z++) {
				gl.glBegin(GL2.GL_TRIANGLE_STRIP);
				for(int x=0; x<imgWidth-1; x++) {
					//Draw Triangle
					//gl.glColor3fv(this.getColour(x, z),0);
					gl.glNormal3fv(this.calculateNormal(x, z),0);
						gl.glVertex3f(x, heightMap[z][x], z);
					//gl.glColor3fv(this.getColour(x, z+1),0);
					gl.glNormal3fv(this.calculateNormal(x, z+1),0);
						gl.glVertex3f(x, heightMap[z+1][x], z+1);
				}
				gl.glEnd();	
			}
		
		gl.glPopMatrix();
		
		//gl.glEndList();
	}
	
	/**
	 * Buffer in image for rendering
	 */
	private void getHeightMap() {
		try {
			BufferedImage img = ImageIO.read(new File("heightMaps/AucklandHeightMap.png"));
			
			imgWidth = img.getWidth();
			imgHeight = img.getHeight();
			heightMap = new float[imgHeight][imgWidth];
			
			for(int y=0; y<imgHeight; y++) {
				for(int x=0; x<imgWidth; x++) {
					int rgb = img.getRGB(x, y);
					int grey = rgb & 255;
					
					heightMap[y][x] = grey/5f;
				}
			}
			
			System.out.println("[DEBUG] Image HeightMap scanned in correctly. "+imgWidth+" x "+imgHeight);
			
		} catch (IOException e) {
			System.out.println("[DEBUG] ERROR - Image HeightMap did NOT scan in correctly");
			
			e.getMessage();
			e.getStackTrace();
		}
	}
	
	//Buffer in texture images for terrain
	private void bufferImages() {
		//Try and scan in grass
		try {
			grassTexture = TextureIO.newTexture(new File("textures/Grass.png"), false);
			System.out.println("[DEBUG] Grass texture loaded correctly. "+imgWidth+" x "+imgHeight);
		}catch(Exception e) {
			System.out.println("[DEBUG] ERROR - Grass texture did NOT load correctly");
		}
		
		//Try and scan in gravel
		try {
			grassTexture = TextureIO.newTexture(new File("textures/Gravel.png"), false);
			System.out.println("[DEBUG] Gravel texture loaded correctly. "+imgWidth+" x "+imgHeight);
		}catch(Exception e) {
			System.out.println("[DEBUG] ERROR - Gravel texture did NOT load correctly");
		}
	}
	
	//Get colour at specific height
	private float[] getColour(int x, int z) {		
		try {			
			if(Math.toDegrees(Math.atan(Math.abs(heightMap[z][x]-heightMap[z+1][x]))) > 45) {
				return new float[]{1,0,0};
			}else {
				return new float[]{1,1,1};
			}
		}catch(Exception e) {
			return new float[]{0,0,0};
		}
	}
	
	//Calculate average vertex normal at x, y, z point
	private float[] calculateNormal(int x, int z) {
		//There's a total of 6 hearest triangles to the index at x, z
		float[] point1;
		float[] point2;
		float[] point3;
		//First
		point1 = new float[]{x-1, this.getHeight(x-1, z), z};
		point2 = new float[]{x, this.getHeight(x, z), z};
		point3 = new float[]{x, this.getHeight(x, z+1), z+1};
		float[] aboveLeftNormal = this.calculateSurfaceNormal(point1, point2, point3);
		//Second
		point1 = new float[]{x, this.getHeight(x, z), z};
		point2 = new float[]{x+1, this.getHeight(x+1, z+1), z+1};
		point3 = new float[]{x, this.getHeight(x, z+1), z+1};
		float[] aboveMiddleNormal = this.calculateSurfaceNormal(point1, point2, point3);
		//Third
		point1 = new float[]{x, this.getHeight(x, z), z};
		point2 = new float[]{x+1, this.getHeight(x+1, z), z};
		point3 = new float[]{x+1, this.getHeight(x+1, z+1), z+1};
		float[] aboveRightNormal = this.calculateSurfaceNormal(point1, point2, point3);
		//Fourth
		point1 = new float[]{x-1, this.getHeight(x-1, z-1), z-1};
		point2 = new float[]{x, this.getHeight(x, z), z};
		point3 = new float[]{x-1, this.getHeight(x-1, z), z};
		float[] bellowLeftNormal = this.calculateSurfaceNormal(point1, point2, point3);
		//Fifth
		point1 = new float[]{x-1, this.getHeight(x-1, z-1), z-1};
		point2 = new float[]{x, this.getHeight(x, z-1), z-1};
		point3 = new float[]{x, this.getHeight(x, z), z};
		float[] bellowMiddleNormal = this.calculateSurfaceNormal(point1, point2, point3);
		//Sixth
		point1 = new float[]{x, this.getHeight(x, z-1), z-1};
		point2 = new float[]{x+1, this.getHeight(x+1, z), z};
		point3 = new float[]{x, this.getHeight(x, z), z};
		float[] bellowRightNormal = this.calculateSurfaceNormal(point1, point2, point3);
		
		//Caculate average normal from all nearby surface normals
		float xForNormal = (aboveLeftNormal[0]+aboveMiddleNormal[0]+aboveRightNormal[0]+bellowLeftNormal[0]+bellowMiddleNormal[0]+bellowRightNormal[0])/
						   (Math.abs(aboveLeftNormal[0])+Math.abs(aboveMiddleNormal[0])+
						    Math.abs(aboveRightNormal[0])+Math.abs(bellowLeftNormal[0])+
						    Math.abs(bellowMiddleNormal[0])+Math.abs(bellowRightNormal[0]));
		float yForNormal = (aboveLeftNormal[1]+aboveMiddleNormal[1]+aboveRightNormal[1]+bellowLeftNormal[1]+bellowMiddleNormal[1]+bellowRightNormal[1])/
						   (Math.abs(aboveLeftNormal[1])+Math.abs(aboveMiddleNormal[1])+
						    Math.abs(aboveRightNormal[1])+Math.abs(bellowLeftNormal[1])+
						    Math.abs(bellowMiddleNormal[1])+Math.abs(bellowRightNormal[1]));
		float zForNormal = (aboveLeftNormal[2]+aboveMiddleNormal[2]+aboveRightNormal[2]+bellowLeftNormal[2]+bellowMiddleNormal[2]+bellowRightNormal[2])/
						   (Math.abs(aboveLeftNormal[2])+Math.abs(aboveMiddleNormal[2])+
						    Math.abs(aboveRightNormal[2])+Math.abs(bellowLeftNormal[2])+
						    Math.abs(bellowMiddleNormal[2])+Math.abs(bellowRightNormal[2]));
		
		//Finally calculate vertex normal
		float[] mainVertexNormal = new float[]{xForNormal, yForNormal, zForNormal};
		
		return mainVertexNormal;
	}
	
	//Get height
	private float getHeight(int x, int z) {
		try {
			return heightMap[z][x];
		}catch(Exception e) {
			return 0;
		}
	}
	
	//Caculate the surface normal at x, y, z
	private float[] calculateSurfaceNormal(float[] firstPoint, float[] secondPoint, float[] thirdPoint) {
		try {
			//Get surface normal of nearest left
			float[] U = new float[]{(thirdPoint[0]-firstPoint[0]),(thirdPoint[1]-firstPoint[1]),(thirdPoint[2]-firstPoint[2])};
			float[] V = new float[]{(secondPoint[0]-firstPoint[0]),(secondPoint[1]-firstPoint[1]),(secondPoint[2]-firstPoint[2])};
			float[] normal = new float[]{
				(U[1]*V[2] - U[2]*V[1]),
				(U[2]*V[0] - U[0]*V[2]),
				(U[0]*V[1] - U[1]*V[0])
			};
			
			return normal;
		}catch(Exception e) {
			return new float[] {0,0,0};
		}
	}
	
}
