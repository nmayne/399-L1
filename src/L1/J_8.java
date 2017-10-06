package L1;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class J_8 {
	public static void main(String[] args) {
		UnregulatedMotor RM = new UnregulatedMotor(MotorPort.A);
		UnregulatedMotor LM = new UnregulatedMotor(MotorPort.D);
		RM.resetTachoCount();
		LM.resetTachoCount();
		System.out.println(RM.getTachoCount());
		System.out.println(LM.getTachoCount());
		//move forward in a line
		RM.setPower(40);
		LM.setPower(40);
		RM.forward();
		LM.forward();
		Delay.msDelay(700);
		RM.setPower(100);
		LM.stop();
		//turn
		Delay.msDelay(2200);
		//forward
		RM.setPower(40);
		LM.forward();
		Delay.msDelay(1400);
		LM.setPower(100);
		RM.stop();
		Delay.msDelay(2200);
		LM.setPower(40);
		RM.forward();
		Delay.msDelay(700);
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
