/*
 * A differential steering robot that takes command arrays, printing its location and bearing as it runs
 * 
 * https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
 */

package L1;


import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.utility.Delay;
import lejos.utility.Matrix;
import lejos.utility.Stopwatch;
import java.io.*;
import java.net.*;

class Robot {
	static double xPos = 0;
	static double yPos = 0;
	static double thetaPos = 0;
	static double yPosUS = 0;
	static double thetaGyro = 0;
	static float US_initial = 0;
	static boolean runProgram = true;
	
	// instantiate motors
	static UnregulatedMotor[] motors = {
		new UnregulatedMotor(MotorPort.D),	// i = 0: right motor (M1)
		new UnregulatedMotor(MotorPort.A)	// i = 1: left motor (M2)
		};
	
	// instantiate sensors
	static EV3UltrasonicSensor sensorUS = new EV3UltrasonicSensor(SensorPort.S1);	// Robot frame x-axis distance
	static EV3GyroSensor sensorGyro = new EV3GyroSensor(SensorPort.S2);				// Robot rotation

	// instantiate timer
	static Stopwatch timer = new Stopwatch();
	
	// drive the robot
	static class Drive implements Runnable{
		int[][] commands;
	
		Drive (int[][] commands){
			this.commands = commands;
		}
		
		@Override
		public void run(){
			timer.reset(); // reset timer for first localization sample, helps keep things in sync
			// execute movement commands
			for(int i = 0; i < commands.length; i++){
				for(int j = 0; j < 2; j++){
					motors[j].setPower(Math.abs(commands[i][j]));
					if(commands[i][j] < 0){
						motors[j].backward();
					} 
					else {
						motors[j].forward();
					}
				}
				Delay.msDelay(commands[i][commands[i].length - 1]);
			}
			
			 // halt motors to prevent overshooting due to slow deceleration after final command
			for(int i=0; i<2; i++){
				motors[i].setPower(0);
			}
			
			// stop sampling 
			runProgram = false;
			Delay.msDelay(100);	// ensure localization and sensors have caught up
			
        	// print calculated location
			printLocation();
			}
	}
	
	// localize the robot
	static class Localize implements Runnable{
		int time;
		int d = 120/2;	// distance from wheel to origin (mm)
		int r = 28;		// radius of wheel (mm)
		int tach_M1;	// tachometer reading of motor 1 (360 = 2pi rad)
		int tach_M2;	// tachometer reading of motor 2 (360 = 2pi rad)
		double[] angVel = new double[2]; // angular velocities of the motors
		
	 	// inverse of constraint matrix, C
		Matrix Cinv = new Matrix(new double[][]{{0, 1, d},		// rolling constraint on wheel 1
												{0, 1, -d},		// rolling constraint on wheel 2
												{-1, 0, 0}		// sliding constraint on wheel 1 <-- sufficient
												}).inverse();	// we need the inverse for calc at end
		
		@Override
		public void run() {
			while(runProgram) {
				Delay.msDelay(100);		// Minimizes error due to motor tach discrepancy.
										// Oversampling w/o delay doesn't allow enough time for
										// tachs to average out. # of samples maximized and
										// magnitude of error minimized at 100 ms delay.
										// Derived by testing impact of sampling rate on accumulated 
										// error between the two tachs during a straight-line drive.
				
				// grab data (tachometer readings and time)
				tach_M1 = motors[0].getTachoCount();	// tach of motor 1
				motors[0].resetTachoCount(); 			// reset tach 1 for next sample
				
				tach_M2 = motors[1].getTachoCount();	// tach of motor 2
				motors[1].resetTachoCount();			// reset tach 2 for next sample
				
				time = timer.elapsed() - 12;			// delta t, through testing, there is a mean error
														// of ~12 ms per sample over time probably due to
														// threading and the imported Stopwatch class.
				timer.reset();							// reset timer for next sample

								
				// calculate angular velocity, w, of each motor, (rad/ms)
				angVel[0] = ((Math.PI * tach_M1) / 180) / time;
				angVel[1] = ((Math.PI * tach_M2) / 180) / time;
				
				// column vector giving linear velocity for the right wheel, 
				// linear velocity for the left wheel, and an initial theta of 0
				// relative to the coordinate frame of the robot chassis
				Matrix W = new Matrix(new double[][]{{r*angVel[0]},{r*angVel[1]},{0}});
							
				// calculate the X, Y, theta of the robot relative to its frame (mm, mm, rad)
				// Robot chassis velocities = inv(C)*W
				Matrix Xi_robot = Cinv.times(W);
				
				// integrate robot's theta as a necessary parameter for the rotation matrix, R.
				thetaPos = thetaPos + (time*Xi_robot.get(2, 0));		
							
				// rotational matrix to translate from robot frame coords to world frame coords
				// this is the most computationally ugly step and could probably be improved!
				//
				// cos(theta)	sin(theta)	0
				// -sin(theta)	cos(theta)	0
				// 		0			0		1	
				Matrix Rinv = new Matrix(new double[][]{
														{Math.cos(thetaPos), Math.sin(thetaPos), 0},
														{-1*Math.sin(thetaPos), Math.cos(thetaPos), 0},
														{0, 0, 1}}).inverse();
				
				// the velocities of the robot rotated to the inertial (world) frame
				// Robot's world velocities = inv(R)*Xi_robot
				Matrix Xi_world = Rinv.times(Xi_robot);
				
				// integrate robot's world velocities over time
				xPos = xPos + time*Xi_world.get(0, 0);
				yPos = yPos + time*Xi_world.get(1, 0);
			}
		}
	}
	
	// get sensor data to localize robot
	static class SensorData implements Runnable{

		@Override
		public void run() {
			float[] sample = new float[2];
			sensorUS.getDistanceMode().fetchSample(sample, 0);;
			US_initial = sample[0]*1000;	// x1000 to give value in mm
			
			while(runProgram) {
				Delay.msDelay(100);	// assumption of same delay from Localize run()

				sensorUS.getDistanceMode().fetchSample(sample, 0);
				sensorGyro.getAngleMode().fetchSample(sample, 1);
				
				// these do not need integration over time so just refresh the current value
				yPosUS = US_initial - sample[0]*1000;	// minus initial (calibrated at y=0)
				thetaGyro = sample[1] * (Math.PI/180);	// converted to rad
			}
		}
	}
	
	// take commands from computer
	static class CommandInput implements Runnable{
		
		@Override
		public void run() {
			// setup TCP
			int power = 60;
			timer.reset(); // reset timer for first localization sample, helps keep things in sync

	        try (
	            ServerSocket serverSocket =
	                new ServerSocket(6003); // initiate server
	            Socket clientSocket = serverSocket.accept();     
	            PrintWriter out =
	                new PrintWriter(clientSocket.getOutputStream(), true);                   
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(clientSocket.getInputStream()));
	        ) {
	            String userCommand;
            	System.out.println("Now Taking Commands");
	            while ((userCommand = in.readLine()) != null) {
	                System.out.println(userCommand);
            		motors[0].setPower(power);
            		motors[1].setPower(power);

	            	switch (userCommand) {
		            	case "a":
		            		// left
			                System.out.println("Left");
		            		motors[0].forward();
		            		motors[1].backward();
		            		break;
		            	case "s":
		            		// reverse
		            		System.out.println("Reverse");
		            		motors[0].backward();
		            		motors[1].backward();
		            		break;
		            	case "d":
		            		// right
		            		System.out.println("Right");
		            		motors[0].backward();
		            		motors[1].forward();
		            		break;
		            	case "w":
		            		// forward
		            		System.out.println("Forward");
		            		motors[0].forward();
		            		motors[1].forward();
		            		break;
		            	case "x":
		            		// halt
		            		System.out.println("Forward");
		            		motors[0].setPower(0);
		            		motors[1].setPower(0);
		            		break;
	            	}
	            	
	            	// print calculated location
	            	printLocation();
	            }
	        } catch (IOException e) {
	            System.out.println("No connection on port 6003");
	            System.out.println(e.getMessage());
	        }
		}
		
	}
	
	// print the localization and sensor data
	static public void printLocation(){
		System.out.println("LOCALIZATION:");
		System.out.printf("\nxPos: %.2f mm "
						+ "\nyPos: %.2f mm "
						+ "\nyUS: %.2f mm"
						+ "\ntheta: %.2f rad"
						+ "\nGyro: %.2f rad\n\n",
						xPos, yPos, yPosUS, thetaPos, thetaGyro);
	}
	
	// main method
	public static void main(String[] args){
		
		// setup a new drive command
		Command commands = new Command();
		
		// setup localization thread;
		Thread localize = new Thread(new Localize(), "Localize");
		localize.setDaemon(true);
		
		// setup sensor thread
		sensorUS.enable();
		sensorGyro.reset();
		Thread sensorData = new Thread(new SensorData(), "SensorData");
		sensorData.setDaemon(true);
		
		// setup command input
		Thread commandInput = new Thread(new CommandInput(), "Commnand Input");
		commandInput.setDaemon(true);
		
		// setup driving thread (see Command class for available commands)
		Thread drive = new Thread(new Drive(commands.getCommand("lab1")), "Drive");
		
		// run threads
		localize.start();
		sensorData.start();
//		commandInput.start();
		drive.start();
		
		Button.waitForAnyPress(); // press any button to exit

	}
}