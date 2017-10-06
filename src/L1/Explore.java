// The robot explores by moving away from the light, but 
// if it gets near the light it slows down and cautiously explores
// instead of running away like cowardice.

package L1;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Explore {
	static EV3UltrasonicSensor LUS = new EV3UltrasonicSensor(SensorPort.S4);	// Robot frame x-axis distance
	static EV3UltrasonicSensor RUS = new EV3UltrasonicSensor(SensorPort.S1);	// Robot frame x-axis distance
	public static SampleProvider leftSensor; 
	public static SampleProvider rightSensor; 
	public static UnregulatedMotor[] motor = {
		new UnregulatedMotor(MotorPort.D),// i = 0: left motor
		new UnregulatedMotor(MotorPort.A)	// i = 1: right motor
		};
	public int decrease = 40;
	public int power = 40;
	Random rand = new Random();
	
	public void explorer() {
		leftSensor = LUS.getDistanceMode();
		rightSensor = RUS.getDistanceMode();
		float[] sampleL = new float[leftSensor.sampleSize()];
		float[] sampleR = new float[rightSensor.sampleSize()];
		double dataL;
		double dataR;
	    while(Button.readButtons() == 0) {  // press any button to exit program
			leftSensor.fetchSample(sampleL, 0);
			rightSensor.fetchSample(sampleR, 0);
			dataL = sampleL[0]*100;
			dataR = sampleR[0]*100;
			System.out.printf("Left = %.2f \nRight = %.2f\n\n", dataL, dataR);
			if(dataL < 20 || dataR < 20) { 
				int value = rand.nextInt(2);  // set either left or right motor randomly
				motor[value].setPower(-100); // turn and continue straight
				Delay.msDelay(500);
			}
			else {
				for(int i = 0; i < 2; i++){
					power = rand.nextInt(100);
					motor[i].setPower(power);
					motor[i].forward();
				}
			}
	    }
	    
    }
	public static void main(String[] args){
		// initialize robot
		Explore robot = new Explore();
		
		Delay.msDelay(1000);
		for(int i = 0; i < 2; i++){ 
			motor[i].setPower(40);
			motor[i].forward();
		}
		robot.explorer();
//		 stop motor function
		for(int i=0; i<2; i++){
			motor[i].close();
		}

		Button.waitForAnyPress();
	}
}
