package org.usfirst.frc.team3309.robot;

import org.usfirst.frc.team3309.subsystems.Drive;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot{
	// Runs when Robot is turned on
		public void robotInit() {
		}

		// When first put into disabled mode
		public void disabledInit() {
		}

		// Called repeatedly in disabled mode
		public void disabledPeriodic() {
		}

		// Init to Auto
		public void autonomousInit() {

		}

		// This function is called periodically during autonomous
		public void autonomousPeriodic() {
		}

		// Init to Tele
		public void teleopInit() {
			
		}

		// This function is called periodically during operator control
		public void teleopPeriodic() {
			// Update the subsystems
			Drive.getInstance().update();
			
			System.out.println("GYRO: " + Sensors.getAngle());
			System.out.println("GYRO RATE: " + Sensors.getAngularVel());
		}
}
