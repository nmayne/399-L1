package robot;
/*
 * A differential steering robot that takes command arrays, printing its location and bearing as it runs
 */

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;


public class Robot {
	
	public static class Drive implements Runnable{
		int[][] commands;
		UnregulatedMotor[] motors;
		Stopwatch timer;
		
		public Drive (int[][] commands, UnregulatedMotor[] motors, Stopwatch timer){
			this.commands = commands;
			this.motors = motors;
			this.timer = timer;
		}
		
		@Override
		public void run(){
			// execute movement commands
			for(int i=0; i<commands.length; i++){
				timer.reset();
				for(int j=0; j<motors.length; j++){
					motors[j].resetTachoCount();
					motors[j].setPower(Math.abs(commands[i][j]));
					if(commands[i][j]<0){
						motors[j].backward();
					} else {
						motors[j].forward();
					}
				}
				Delay.msDelay(commands[i][commands[i].length-1]*1000);
			}
		}
	}

	public static class Localize implements Runnable{
		private UnregulatedMotor[] motors;
		private Stopwatch timer;
//		private int time;
//		private int r = 21; //radius of wheel
//		private int D = 76; //distance to center
//		private double pi = 22/7;
//		private double thetaL;
//		private double thetaR;
//		private double vL;
//		private double vR;
//		private double vC;
//		private double wC;
//		private double deltaX;
//		private double deltaY;
//		private double xK = 0;
//		private double yK = 0;
//		private double thetaK = 0;
//		private double leftTC;
//		private double rightTC;
		
		// constructor
		public Localize(UnregulatedMotor[] motors, Stopwatch timer){
			this.motors = motors;
			this.timer = timer;
		}
		
		// run this thread
		@Override
		public void run() {
			while(true){
				for(int i=0; i<motors.length; i++){
					System.out.format("Time: %d  Motor %d Tach: %d\n", timer.elapsed(), i+1, motors[i].getTachoCount());			
				}
				System.out.println();
				Delay.msDelay(1000);
				
				// Localization Calculations
//				time = timer.elapsed();
//				
//				thetaL = pi*motors[0].getTachoCount()/180;
//				thetaR = pi*motors[1].getTachoCount()/180;
//				
//				vL = linearVelocity(r,thetaL,time); 
//				vR = linearVelocity(r,thetaR,time);
//	
//				vC = (vL+vR)/2;
//				wC = (vR-vL)/2*D;
//				
//				deltaY = (vC*time)*Math.sin(wC);
//				deltaX = (vC*time)*Math.cos(wC);
//				
//				xK = xK + deltaX;
//				yK = yK + deltaY;
//				thetaK = thetaK + wC;
//				
//				System.out.print("\ndeltaY =  " + deltaY + "\ndeltaX = " + deltaX + "\nthetaK = " + wC +"\n");
			}
		}

//		private double linearVelocity(int r, double theta, int time) {
//			return (r*theta)/time;
//		}

	}
	
	public static class Input implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub GET USER INPUT TO DRIVE BOT
			
		}
		
	}
	
	public static void main(String[] args){
		
		// setup motors
		UnregulatedMotor[] motors = {
				new UnregulatedMotor(MotorPort.A),	// i=0: left motor
				new UnregulatedMotor(MotorPort.D)	// i=1: right motor
		};
		
		// setup a new action command
		Command command = new Command();
		
		// setup a timer
		Stopwatch timer = new Stopwatch();
		
		// begin localization thread;
		Thread localize = new Thread(new Localize(motors, timer),"Localize");
		localize.setDaemon(true);
		localize.start();
		
		// begin driving thread
		Thread drive = new Thread(new Drive(command.getCommand("figureEight"), motors, timer), "Drive");
		drive.start();
		
		// end motor function
		for(int i=0; i<motors.length; i++){
			motors[i].close();
		}

	}
}