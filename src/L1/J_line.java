package L1;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.hardware.motor.UnregulatedMotor;

public class J_line {
	public static void main(String[] args) {
		UnregulatedMotor RM = new UnregulatedMotor(MotorPort.A);
		UnregulatedMotor LM = new UnregulatedMotor(MotorPort.D);
		RM.resetTachoCount();
		LM.resetTachoCount();
		System.out.println(RM.getTachoCount());
		System.out.println(LM.getTachoCount());
		//move forward in a line
		RM.setPower(60);
		LM.setPower(60);
		RM.forward();
		LM.forward();
		Delay.msDelay(1250);
		RM.stop();
		LM.stop();
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