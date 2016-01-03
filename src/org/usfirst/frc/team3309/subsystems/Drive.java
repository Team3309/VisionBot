package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.drive.DriveEncodersController;
import org.team3309.lib.controllers.drive.equations.DriveBasicEquationController;
import org.team3309.lib.controllers.generic.BlankController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.Victor;

public class Drive extends ControlledSubsystem {

	double MAX_ANG_VEL = 338, MAX_TRANS_VEL = 0;
	double MAX_ANG_ACC = 100, MAX_TRANS_ACC = 0;
	/**
	 * Used to give a certain gap that the drive would be ok with being within
	 * its goal encoder average.
	 */
	private static final double DRIVE_ENCODER_LENIENCY = 40;

	/**
	 * Used to give a certain gap that the drive would be ok with being within
	 * its goal angle
	 */
	private static final double DRIVE_GYRO_LENIENCY = 10;

	private static Drive instance;
	private Victor leftFront = new Victor(RobotMap.LEFT_FRONT_MOTOR);
	private Victor rightFront = new Victor(RobotMap.RIGHT_FRONT_MOTOR);
	private Victor leftBack = new Victor(RobotMap.LEFT_BACK_MOTOR);
	private Victor rightBack = new Victor(RobotMap.RIGHT_BACK_MOTOR);
	
	private double x = 0;
	private double y = 0;
	private double pastAngVel = Sensors.getAngularVel();
	private double pastTransVel = Sensors.getTransVel();
	private double pastTrans = 0;
	
	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Drive getInstance() {
		if (instance == null)
			instance = new Drive("Drive");
		return instance;
	}

	private Drive(String name) {
		super(name);
		// mController = new DriveCheezyDriveEquation();
		mController = new DriveBasicEquationController();
	}

	private void trackPosition() {
		 this.x += KragerMath.cosDeg(Sensors.getAngle()) * (Sensors.getTrans() - pastTrans);
		 this.y += KragerMath.sinDeg(Sensors.getAngle()) * (Sensors.getTrans() - pastTrans);
		 
	}
	// Sets controller based on what state the remotes and game are in
	private void updateController() {
		// if mController is Completed and has not already been made blank, then
		// make it blank
		if (mController.isCompleted() && !(mController instanceof BlankController)) {
			mController = new BlankController();
		}
	}

	@Override
	public void update() {
		updateController();
		trackPosition();
		OutputSignal output = mController.getOutputSignal(getInputState());
		setLeftRight(output.getLeftMotor(), output.getRightMotor());
		// Let me FIND MAX
		if (Sensors.getAngularVel() > MAX_ANG_VEL) {
			MAX_ANG_VEL = Sensors.getAngularVel();
			Drive.instance.print("ANG MAX " + MAX_ANG_VEL);
		}
		if (Sensors.getTransVel() > this.MAX_TRANS_VEL) {
			MAX_TRANS_VEL = Sensors.getTransVel();
			Drive.instance.print("TRANS MAX " + MAX_TRANS_VEL);
		}

		if (Sensors.getAngularVel() - pastAngVel > MAX_ANG_ACC) {
			MAX_ANG_ACC = Sensors.getAngularVel() - pastAngVel;
			Drive.instance.print("ANG AC MAX " + MAX_ANG_ACC);
		}
		if (Sensors.getTransVel() - pastTransVel > this.MAX_TRANS_ACC) {
			MAX_TRANS_ACC = Sensors.getTransVel() - pastTransVel;
			Drive.instance.print("TRANS AC MAX " + MAX_TRANS_ACC);
		}
		// Drive.instance.print(" " + Sensors.getTrans());
		pastAngVel = Sensors.getAngularVel();
		pastTransVel = Sensors.getTransVel();
		pastTrans = Sensors.getTrans();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setAngularPos(Sensors.getAngle());
		input.setAngularVel(Sensors.getAngularVel());
		input.setLeftPos(Sensors.leftDrive.getDistance());
		input.setLeftVel(Sensors.leftDrive.getRate());
		input.setRightVel(Sensors.rightDrive.getDistance());
		input.setRightPos(Sensors.rightDrive.getRate());
		input.setX(x);
		input.setY(y);
		return input;
	}

	/**
	 * Creates and runs a controller that gets the drive to the given setoiunt
	 * 
	 * @param encoders
	 *            goal encoder values
	 */
	public void setSetpoint(double encoders) {
		mController = new DriveEncodersController(encoders);
	}

	/**
	 * Returns the Angle the robot is at
	 * 
	 * @return Angle robot is at
	 */
	public double getAngle() {
		return Sensors.getAngle();
	}

	/**
	 * Returns the average of the two encoders to see the ditstance traveled
	 * 
	 * @return the average of the left and right to get the distance traveled
	 */
	public double getDistanceTraveled() {
		return (Sensors.leftDrive.get() + Sensors.rightDrive.get()) / 2;
	}

	/**
	 * returns if the current average of encoders (aka distance traveled) is
	 * close to the encoderGoal. Uses DRIVE_ENCODER_LENIENCY to tell if it is
	 * close.
	 * 
	 * @param encoderGoal
	 *            Encoder drive should be at
	 * @return
	 */
	public boolean isEncoderCloseTo(double encoderGoal) {
		if (getDistanceTraveled() < encoderGoal + DRIVE_ENCODER_LENIENCY
				&& getDistanceTraveled() > encoderGoal - DRIVE_ENCODER_LENIENCY) {
			return true;
		}
		return false;
	}

	/**
	 * returns if the current angle is close to the angleGoal. Uses
	 * DRIVE_ANGLE_LENIENCY to tell if it is close.
	 * 
	 * @param angleGoal
	 *            Angle drive should be at
	 * @return
	 */
	public boolean isAngleCloseTo(double angleGoal) {
		if (getAngle() < angleGoal + DRIVE_GYRO_LENIENCY && getDistanceTraveled() > angleGoal - DRIVE_GYRO_LENIENCY) {
			return true;
		}
		return false;
	}

	/**
	 * Stops current running controller and sets motors to zero
	 */
	public void stopDrive() {
		mController = new BlankController();
		setLeftRight(0, 0);
	}

	/**
	 * Sets motors left then right
	 * 
	 * @param left
	 *            leftMotorSpeed
	 * @param right
	 *            rightMotorSpeed
	 */
	private void setLeftRight(double left, double right) {
		setRightLeft(right, left);
	}

	/**
	 * Sets motors right then left
	 * 
	 * @param right
	 *            rightMotorSpeed
	 * @param left
	 *            leftMotorSpeed
	 */
	private void setRightLeft(double right, double left) {
		setLeft(left);
		setRight(right);
	}

	/**
	 * Sets the right side of the drive
	 * 
	 * @param right
	 *            rightMotorSpeed
	 */
	private void setRight(double right) {
		rightFront.set(-right);
		rightBack.set(-right);
	}

	/**
	 * Sets the left side of the drive
	 * 
	 * @param left
	 *            leftMotorSpeed
	 */
	private void setLeft(double left) {
		leftFront.set(left);
		leftBack.set(left);
	}

	public double getMAX_ANG_VEL() {
		return MAX_ANG_VEL;
	}

	public double getMAX_TRANS_VEL() {
		return MAX_TRANS_VEL;
	}

	@Override
	public void sendToSmartDash() {
		mController.sendToSmartDash();
	}

	public double getMAX_TRANS_ACC() {
		return MAX_TRANS_ACC;
	}

	public double getMAX_ANG_ACC() {
		return MAX_ANG_ACC;
	}
}