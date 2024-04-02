package frc.robot.commands;

import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class CloseShot extends FixedShot {

    public CloseShot(Shooter shooter, WristJoint wrist, Indexer indexer, Intake intake) {
        super(shooter, wrist, indexer, intake, 44, 50,
            CrabbyConstants.WristConstants.closeShotRotation, CrabbyConstants.WristConstants.tolerance, CrabbyConstants.IndexerConstants.indexerShootSpeed,
            CrabbyConstants.IntakeConstants.intakeSpeed, CrabbyConstants.ShooterConstants.clearanceTime);
    }
}
