package L1;

/*
 * A command for a differential steering robot with two motors and drive-time
 */
public class Command {
	
	// PART 2 (NOTE: for the actual programs used for PART 2 see the individual .java files named by the shape)
	// these are merely a demonstration for the robot with the embodiement used for PART 3, 4 and 6.
	// command to move forward in a rectangle pattern
	final private int[][] rect = {
			{60, 60, 1200}, // L1
			{60, -60, 475}, // Turn CCW
			{60, 60, 800},	// W1
			{60, -60, 475},	// Turn CCW
			{60, 60, 1200}, // L2
			{60, -60, 475}, // Turn CCW
			{60, 60, 800},	// W2
			{60, -60, 475}, // Turn CCW
			};
	
	// command to move in a circle
	final private int[][] circle = {{80, -10, 2600}};
	
	// command to move in a figure-eight
	final private int[][] figureEight = {
			{40, 40, 700},	// Enter first 'circle'
			{80, -10, 1500}, // C1
			{40, 40, 1400},	// Enter second 'circle'
			{80, -10, 1500}, // C2
			{40, 40, 700},	// Back to center
			};
	

	// PART 3
	// Dead reckoning sample command
	final private int[][] lab1 = {
			{ 80, 60, 2000},
			{ 60, 60, 1000},
			{-50, 80, 2000}
			};
	
	// PART 4
	// command to move forward in a straight line, power 30
	final private int[][] lineA = {
			{30, 30, 1500}};
	
	// command to move forward in a straight line, power 60
	final private int[][] lineB = {
			{60, 60, 1500}};
	
	// command to move forward in a straight line, power 90
	final private int[][] lineC = {
			{90, 90, 1500}};
	
	// command to rotate about the ICC, power 30
	final private int[][] rotA = {
			{30, -30, 1500}};
	
	// command to rotate about the ICC, power 30
		final private int[][] rotB = {
				{60, -60, 1500}};
		
	// command to rotate about the ICC, power 30
		final private int[][] rotC = {
				{90, -90, 1500}};
	
	
	

	
	
	// a user-defined command
	private int[][] user = {};
	
	// default constructor
	public Command(){};
	
	/*
	 * user-defined constructor takes a 2D int array
	 * specifying left motor power, right motor power,
	 * and the duration (ms) to run the command, e.g.
	 * {{LM_P, RM_P, Duration}}
	 */
	public Command(int[][] user){
		this.user = user; 
	}
	
	public void setCommand(int[][] user){
		this.user = user;
	}
	
	public int[][] getCommand(String command){
		switch(command) {
		
		// PART 1
		case "rect": return this.rect;
		case "circle": return this.circle;
		case "figureEight": return this.figureEight;
		
		// PART 3
		case "lab1": return this.lab1;
		
		// PART 4
		case "lineA": return this.lineA;
		case "lineB": return this.lineB;
		case "lineC": return this.lineC;
		
		case "rotA": return this.rotA;
		case "rotB": return this.rotB;
		case "rotC": return this.rotC;

		default: return this.user;
		}
	}
}