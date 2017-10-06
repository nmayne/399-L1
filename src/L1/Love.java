// The robot exhibits love by moving towards the light, then stops at a perfect distance

package L1;

import java.util.Random;
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class Love {
	EV3ColorSensor LCS = new EV3ColorSensor(SensorPort.S4); // left sensor
	EV3ColorSensor RCS = new EV3ColorSensor(SensorPort.S1); // right sensor
	public static SensorMode leftSensor; 
	public static SensorMode rightSensor; 
	public static UnregulatedMotor[] motor = {
		new UnregulatedMotor(MotorPort.D),// i = 0: left motor
		new UnregulatedMotor(MotorPort.A)	// i = 1: right motor
		};
	public int power = 30;
	Random rand = new Random();
	
	public void heart() {
		leftSensor = LCS.getAmbientMode();
		rightSensor = RCS.getAmbientMode();
		float[] sampleL = new float[leftSensor.sampleSize()];
		float[] sampleR = new float[rightSensor.sampleSize()];
		double dataL;
		double dataR;
		int diff;
		int increase;
		int decrease;
        while(Button.readButtons() == 0) {  // press any button to exit program
			leftSensor.fetchSample(sampleL, 0);
			rightSensor.fetchSample(sampleR, 0);
			dataL = sampleL[0]*100;
			dataR = sampleR[0]*100;
//			System.out.printf("Left = %.2f \nRight = %.2f\n\n", dataL, dataR);

			if(dataL >= 55 || dataR >= 55) {
				System.out.print("\n<3\n");
				break;
			} // found light <3
			diff = (int)(dataL - dataR);
			increase = power + (2*Math.abs(diff));
			decrease = power - Math.abs(diff);
			if(diff < 0) { // detects light to the right, left motor increases
				motor[0].setPower(increase);
				motor[1].setPower(decrease);
				Delay.msDelay(1000);
			}
			else if(diff > 0) { // detects light to the left, right motor increases
				motor[1].setPower(increase);
				motor[0].setPower(decrease);
				Delay.msDelay(1000);
			}	
			else { // difference negligible
				int value = rand.nextInt(2);  
				// System.out.print(value); // set either left or right motor randomly
				motor[value].setPower(-80); // turn and continue straight
				Delay.msDelay(200);} 
//			System.out.printf("Power = %d \nIncrease = %d\n\n", power, increase);
			}
			for(int i = 0; i < 2; i++){
				motor[i].setPower(power);
				motor[i].forward();
			}
    }
	public static void main(String[] args){
		// initialize robot
		Love robot = new Love();
		Delay.msDelay(1000);
		for(int i = 0; i < 2; i++){ 
			motor[i].setPower(30);
			motor[i].forward();
		}
		robot.heart();
//		 stop motor function
		for(int i=0; i<2; i++){
			motor[i].close();
		}
		Button.waitForAnyPress();
	}
}
