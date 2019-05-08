package Timing;
/**
 * 
 * @author Nikkolas Diehl - 16945724
 *
 */
public class Timing {
	
	public double thisTick;
	public double prevTick;
	public double delta;
	
	public Timing() {
		thisTick = System.currentTimeMillis()/1000.0;
		prevTick = thisTick;
	}

	public void count() {
		thisTick = System.currentTimeMillis()/1000.0;
		delta = thisTick-prevTick;
		prevTick = thisTick;
	}
}
