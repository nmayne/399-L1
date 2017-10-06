package L1;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class j_circle {
	public static void main(String[] args) {
		UnregulatedMotor RM = new UnregulatedMotor(MotorPort.A);
		UnregulatedMotor LM = new UnregulatedMotor(MotorPort.D);
		RM.resetTachoCount();
		LM.resetTachoCount();
		System.out.println(RM.getTachoCount());
		System.out.println(LM.getTachoCount());
		//move forward in a line
		RM.setPower(70);
		RM.forward();
		Delay.msDelay(4600);
		RM.stop();
		//display tacho count
		System.out.println(RM.getTachoCount());
		System.out.println(LM.getTachoCount());
		RM.resetTachoCount();
		LM.resetTachoCount();
		RM.close();
		LM.close();
		Button.waitForAnyPress();
	}
}
