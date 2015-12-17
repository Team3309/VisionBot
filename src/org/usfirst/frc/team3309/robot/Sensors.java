package org.usfirst.frc.team3309.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;

public class Sensors {
	public static Gyro gyro = new Gyro(RobotMap.GYRO_ANALOG_PORT);
	public static Encoder leftDrive = new Encoder(RobotMap.ENCODERS_A_LEFT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_LEFT_DRIVE_DIGITAL, false);
	public static Encoder rightDrive = new Encoder(RobotMap.ENCODERS_A_RIGHT_DRIVE_DIGITAL,
			RobotMap.ENCODERS_B_RIGHT_DRIVE_DIGITAL, false);
	public static Encoder transDrive = new Encoder(RobotMap.FOLLOWER_ENCODER_A, RobotMap.FOLLOWER_ENCODER_B);

	public static double getAngularVel() {
		return gyro.getRate();
	}

	public static double getAngle() {
		return gyro.getAngle();
	}

	public static double getTrans() {
		return transDrive.getDistance();
	}

	public static double getTransVel() {
		return transDrive.getRate();
	}

}
