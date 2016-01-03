package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class DriveCurvatureFollower extends Controller {

	private FeedForwardWithPIDController translationalController = new FeedForwardWithPIDController();
	private FeedForwardWithPIDController angularController = new FeedForwardWithPIDController();
	private Pose[] path;
	private int currentGoalIndex = 0;
	
	public DriveCurvatureFollower(Pose[] path) {
		this.path = path;
	}
	
	@Override
	public void reset() {
		
	}


	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		OutputSignal output = new OutputSignal();
		double delta x
		double deltax = path[currentGoalIndex].x - inputState.getX();
		double deltay = path[currentGoalIndex].y - inputState.getY();
		double lookaheadSquared = Math.pow((path[currentGoalIndex].x - inputState.getX()), 2) + Math.pow(path[currentGoalIndex].y - inputState.getY(), 2);
		double curvature = (2*deltax)/(lookaheadSquared);
		double aimTransVel;
		if (deltay < 0) {
			aimTransVel = -10;
		}else {
			aimTransVel = 10;
		}
		double aimAngVel = curvature * aimTransVel;
		double rightSide = translationalController.getOutputSignal(inputState).getMotor() + angularController.getOutputSignal(inputState).getMotor();
		double leftSide = translationalController.getOutputSignal(inputState).getMotor() - angularController.getOutputSignal(inputState).getMotor();
		output.setLeftRightMotor(leftSide, rightSide);
		return output;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

	public double[][] getPath() {
		return path;
	}

	public void setPath(double path[][]) {
		this.path = path;
	}

}
