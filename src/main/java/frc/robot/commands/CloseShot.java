package frc.robot.commands;

import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class CloseShot extends FixedShot {

    public CloseShot(Shooter shooter, WristJoint wrist, Intake intake) {
        super(shooter, wrist, intake, CrabbyConstants.ShooterConstants.closeShotSpeed, CrabbyConstants.ShooterConstants.tolerance,
            CrabbyConstants.WristConstants.closeShotRotation, CrabbyConstants.WristConstants.tolerance, CrabbyConstants.IntakeConstants.intakeSpeed,
            CrabbyConstants.ShooterConstants.clearanceTime);
    }
}
