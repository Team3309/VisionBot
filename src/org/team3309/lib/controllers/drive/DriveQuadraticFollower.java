package org.team3309.lib.controllers.drive;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.subsystems.Drive;

public class DriveQuadraticFollower extends Controller {

	public int A, B, C;
	public FeedForwardWithPIDController rightSideController = new FeedForwardWithPIDController(1/Drive.getInstance().getMAX_TRANS_VEL(), 1/Drive.getInstance().getMAX_TRANS_ACC(), 0, 0, 0);
	//public FeedForwardWithPIDController leftSideController = new FeedForwardWithPIDController();
	//public FeedForwardWithPIDController angularController = new FeedForwardWithPIDController();
	public DriveQuadraticFollower(int A, int B, int C) {
		this.A = A;
		this.B = B;
		this.C = C;
	}

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double t = inputState.getTime();
		//double aimVel = 
		return null;
	}

	@Override
	public boolean isCompleted() {

		return false;
	}

}
