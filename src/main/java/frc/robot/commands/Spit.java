package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class Spit extends SequentialCommandGroup {
    public Spit(Shooter shooter, WristJoint wrist, Indexer indexer, Intake intake)
    {
        addCommands(
            new ParallelCommandGroup(
                new RampShooterWithDifferential(shooter, () -> 30, () -> 25)
                    .until(() -> shooter.isRunning() && shooter.isAtSpeed(3)),
                new NFRRotatingArmJointSetAngle(wrist, Rotation2d.fromDegrees(22), Rotation2d.fromDegrees(3), 0, true)
            ),
            new ShootIndexerAndIntake(indexer, intake, CrabbyConstants.IndexerConstants.indexerShootSpeed, CrabbyConstants.IntakeConstants.intakeSpeed)
        );
    }
}
