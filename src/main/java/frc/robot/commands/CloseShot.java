package frc.robot.commands;

import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class CloseShot extends FixedShot {

    public CloseShot(Shooter shooter, WristJoint wrist, Indexer indexer, Intake intak) {
        super(shooter, wrist, indexer, intak, CrabbyConstants.ShooterConstants.closeShotSpeed, CrabbyConstants.ShooterConstants.tolerance,
            CrabbyConstants.WristConstants.closeShotRotation, CrabbyConstants.WristConstants.tolerance, CrabbyConstants.IndexerConstants.indexerShootSpeed,
            CrabbyConstants.ShooterConstants.clearanceTime);
    }
}
