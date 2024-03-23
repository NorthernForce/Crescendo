package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;

public class AutoShot extends SequentialCommandGroup {
    public AutoShot(SwerveDrive drive, Shooter shooter, WristJoint wristJoint, Intake intake, Indexer indexer, DoubleSupplier topSpeed, DoubleSupplier bottomSpeed, Supplier<Rotation2d> wristSupplier)
    {
        addCommands(
            new ParallelCommandGroup(
                new AutoPrepShot(drive, shooter, wristJoint, topSpeed, bottomSpeed, wristSupplier),
                Commands.waitUntil(() -> drive.getSpeed() < 0.5 && shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            ),
            new ShootIndexerAndIntake(indexer, intake, CrabbyConstants.IndexerConstants.indexerSpeed, CrabbyConstants.IntakeConstants.intakeSpeed)
        );
        addRequirements(shooter, intake, indexer, wristJoint);
    }
}
