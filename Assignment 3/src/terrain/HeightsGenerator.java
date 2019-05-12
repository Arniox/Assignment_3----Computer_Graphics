package terrain;

import java.util.Random;

public class HeightsGenerator {
	//Height generation
	private static final float AMPLITUDE = 70f;
	
	//Seed
	private Random random = new Random();
	private int seed;
	
	//Constructor
	public HeightsGenerator() {
		//set seed
		this.seed = random.nextInt(1000000000);
	}
	
	//Generate the height value at x, z
	public float generateHeight(int x, int z) {
		//Add together different height frequencies to get a more natural looking terrain
		float total = getInterpolatedNoise(x/16f, z/16f) * AMPLITUDE;
		total += getInterpolatedNoise(x/8f, z/8f) * AMPLITUDE/3f;
		total += getInterpolatedNoise(x/4f, z/4f) * AMPLITUDE/9f;
		total += getInterpolatedNoise(x/2f, z/2f) * AMPLITUDE/27f;
		total += getInterpolatedNoise(x, z) * AMPLITUDE/81f;
		
		return total;
	}
	
	//Get smoother and interpolated noise
	private float getInterpolatedNoise(float x, float z) {
		//Get x and z as ints (eg: 2.7 and 5.1 input = 2 and 5)
		int intX = (int) x;
		int intZ = (int) z;
		//Get their fractions as floats (eg: 2.7 and 5.1 input = 0.7 and 0.1)
		float fracX = x-intX;
		float fracZ = z-intZ;
		
		//Get the nearby vertices of intX, intZ
		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX+1, intZ);
		float v3 = getSmoothNoise(intX, intZ+1);
		float v4 = getSmoothNoise(intX+1, intZ+1);
		
		//Interpolate between v1 and v2 and v3 and v4
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		
		//Return the interpolation between i1 and i2
		return interpolate(i1, i2, fracZ);
	}
	
	//Interpolate between noise points
	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float)(1f-Math.cos(theta))*0.5f;
		
		return a*(1f-f) + b * f;
	}
	
	//Smooth out the noise
	private float getSmoothNoise(int x, int z) {
		//Smooth out the noise
		//corners have least effect on the terrain so divide the corner average by 16f
		//sides have slightly more effect on the terrain so divide the side average by only 8f
		//center is most important so only divide by 4
		float corners = (getNoise(x-1, z-1) + getNoise(x+1, z-1) + getNoise(x-1, z+1) + getNoise(x+1, z+1))/16f;
		float sides = (getNoise(x-1, z) + getNoise(x+1, z) + getNoise(x, z-1) + getNoise(x, z+1))/8f;
		float centre = getNoise(x, z)/4f;
		
		return corners + sides + centre;
	}
	
	//Get noise with a specific seed
	public float getNoise(int x, int z) {
		random.setSeed(x * 49632 + z * 325176 + seed);
		return random.nextFloat() * 2f - 1f;
	}
}
