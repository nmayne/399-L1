// The robot exhibits aggression by moving towards the light, then continuously attacks it once 
// the light reaches a close enough distance. 

package L1;

	import java.util.Random;
	import lejos.hardware.Button;
	import lejos.hardware.motor.UnregulatedMotor;
	import lejos.hardware.port.MotorPort;
	import lejos.hardware.port.SensorPort;
	import lejos.hardware.sensor.EV3ColorSensor;
	import lejos.hardware.sensor.SensorMode;
	import lejos.utility.Delay;

	public class aggression {
		EV3ColorSensor LCS = new EV3ColorSensor(SensorPort.S4); // left sensor
		EV3ColorSensor RCS = new EV3ColorSensor(SensorPort.S1); // right sensor
		public static SensorMode leftSensor; 
		public static SensorMode rightSensor; 
		public static UnregulatedMotor[] motor = {
			new UnregulatedMotor(MotorPort.D),// i = 0: left motor
			new UnregulatedMotor(MotorPort.A)	// i = 1: right motor
			};
		public int power = 30; // set steady roaming power
		Random rand = new Random(); // set up random integer to implement turning in equal light
		
		public void anger() {
			leftSensor = LCS.getAmbientMode(); // set up ambient mode to grab samples
			rightSensor = RCS.getAmbientMode();
			float[] sampleL = new float[leftSensor.sampleSize()];
			float[] sampleR = new float[rightSensor.sampleSize()];
			double dataL; 
			double dataR;
			int diff;
			int increase;
			int decrease;
	        while(Button.readButtons() == 0) {  // press any button to exit program
				leftSensor.fetchSample(sampleL, 0); // fetch sample from left and right sensors for testing
				rightSensor.fetchSample(sampleR, 0);
				dataL = sampleL[0]*100;
				dataR = sampleR[0]*100;
//				System.out.printf("Left = %.2f \nRight = %.2f\n\n", dataL, dataR);

				if(dataL >= 60 || dataR >= 60) { // close to the light, attack
					System.out.print("\nATTACK!!!\n");
					while(Button.readButtons() == 0) { // attack until button press
						motor[0].setPower(100);
						motor[1].setPower(100);
						Delay.msDelay(900);
						motor[0].setPower(-50);
						motor[1].setPower(-50);
						Delay.msDelay(1000);
					} // found light... ATTACK
				}
				diff = (int)(dataL - dataR); // use difference in data to set appropriate power
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
					int value = rand.nextInt(2);   // set either left or right motor randomly
					System.out.print(value);
					motor[value].setPower(-80); // turn and continue straight
					Delay.msDelay(200);} 
//				System.out.printf("Power = %d \nIncrease = %d\n\n", power, increase);
				}
				for(int i = 0; i < 2; i++){
					motor[i].setPower(power);
					motor[i].forward();
				}
	        }

		public static void main(String[] args){
			// initialize robot
			aggression robot = new aggression();
			Delay.msDelay(1000);
			for(int i = 0; i < 2; i++){ 
				motor[i].setPower(30);
				motor[i].forward();
			}
			robot.anger();
//			 stop motor function
			for(int i=0; i<2; i++){
				motor[i].close();
			}

			Button.waitForAnyPress();
		}
	}
