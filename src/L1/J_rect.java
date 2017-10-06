package L1;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.hardware.motor.UnregulatedMotor;

public class J_rect {
	public static void main(String[] args) {
		UnregulatedMotor RM = new UnregulatedMotor(MotorPort.A);
		UnregulatedMotor LM = new UnregulatedMotor(MotorPort.D);
		RM.resetTachoCount();
		LM.resetTachoCount();
		System.out.println(RM.getTachoCount());
		System.out.println(LM.getTachoCount());

		for(int i = 0; i < 2; i++) {
			//move forward 20cm
			RM.setPower(60);
			LM.setPower(60);
			RM.forward();
			LM.forward();
			Delay.msDelay(1000);
			RM.stop();
			LM.stop();
			//display tacho count
			System.out.println(RM.getTachoCount());
			System.out.println(LM.getTachoCount());
			RM.resetTachoCount();
			LM.resetTachoCount();
			//turn left
			Delay.msDelay(200);
//			LM.setPower(61);
			RM.forward();
			LM.backward();
			Delay.msDelay(645);
			RM.stop();
			LM.stop();
			//display tacho count
			System.out.println(RM.getTachoCount());
			System.out.println(LM.getTachoCount());
			RM.resetTachoCount();
			LM.resetTachoCount();
			LM.setPower(60);
			//move forward 10cm 
			RM.forward();
			LM.forward();
			Delay.msDelay(500);
			RM.stop();
			LM.stop();
			//display tacho count
			System.out.println(RM.getTachoCount());
			System.out.println(LM.getTachoCount());
			RM.resetTachoCount();
			LM.resetTachoCount();
			//turn left
			Delay.msDelay(200);
//			LM.setPower(61);
			RM.forward();
			LM.backward();
			Delay.msDelay(645);
			RM.stop();
			LM.stop();
			//display tacho count
			System.out.println(RM.getTachoCount());
			System.out.println(LM.getTachoCount());
			RM.resetTachoCount();
			LM.resetTachoCount();	
		}
		RM.close();
		LM.close();
		Button.waitForAnyPress();
	}
}