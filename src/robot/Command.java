package robot;

/*
 * A command for a differential steering robot with two motors and drive-time
 */
public class Command {
	
	// command to move forward in a straight line
	final private int[][] line = {{60, 60, 1250}};
	
	// command to move forward in a rectangle pattern
	final private int[][] rect = {
			{60, 60, 1000}, // L1
			{60, -60, 645}, // Turn CW
			{60, 60, 500},	// W1
			{60, -60, 645},	// Turn CW
			{60, 60, 1000}, // L2
			{60, -60, 645}, // Turn CW
			{60, 60, 500},	// W2
			{60, -60, 645}, // Turn CW
			};
	
	// command to move in a circle
	final private int[][] circle = {{70, 0, 4600}};
	
	// command to move in a figure-eight
	final private int[][] figureEight = {
			{40, 40, 700},	// Enter first 'circle'
			{0, 100, 2200}, // C1
			{40, 40, 1400},	// Enter second 'circle'
			{0, 100, 2200}, // C2
			{40, 40, 700},	// Back to center
			};
	
	// command to do the sample command from lab 1
	final private int[][] lab1 = {
			{ 80, 60, 2},
			{ 60, 60, 1},
			{-50, 80, 2}
			};
	
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
		case "line": return this.line;
		case "rect": return this.rect;
		case "circle": return this.circle;
		case "figureEight": return this.figureEight;
		case "lab1": return this.lab1;
		default: return this.user;
		}
	}
}