package org.usfirst.frc.team3309.robot;

import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.subsystems.Drive;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
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
		//Drive.getInstance().setController(new DriveEncodersController(5000));
		/*if (Controls.driverController.getA()) {
			Drive.getInstance().setLeft(.4);
		} else if (Controls.driverController.getB()) {
			Drive.getInstance().setLeft(.6);
		} else if (Controls.driverController.getXBut()) {
			Drive.getInstance().setLeft(.8);
		} else if (Controls.driverController.getYBut()) {
			Drive.getInstance().setLeft(1);
		} else {
			Drive.getInstance().setLeft(0);
		}*/
	}
}