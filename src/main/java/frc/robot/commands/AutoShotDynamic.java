package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.TargetingCalculator;

public class AutoShotDynamic extends SequentialCommandGroup {
    public AutoShotDynamic(SwerveDrive drive, Intake intake, Indexer indexer, WristJoint wrist,
        TargetCamera camera, Shooter shooter, DoubleSupplier lastRecordedDistance, TargetingCalculator topSpeedCalculator, TargetingCalculator bottomSpeedCalculator,
        TargetingCalculator wristCalculator)
    {
        addCommands(
            new ParallelCommandGroup(
                new TurnToTargetDynamic(drive,
                    () -> camera.getSpeakerTag()),
                new RampShooterWithDifferential(shooter,
                    () -> topSpeedCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()),
                    () -> bottomSpeedCalculator.getValueForDistance(lastRecordedDistance.getAsDouble())),
                new NFRWristContinuousAngle(wrist, () -> Rotation2d.fromRadians(wristCalculator.getValueForDistance(lastRecordedDistance.getAsDouble())))
            ).until(
                () -> camera.getSpeakerTag().isPresent() && shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance)
                    && CrabbyConstants.DriveConstants.turnToTargetController.atSetpoint() && drive.getSpeed() <= CrabbyConstants.DriveConstants.maxShootSpeed
            ),
            new ShootIndexerAndIntake(indexer, intake, CrabbyConstants.IndexerConstants.indexerShootSpeed, CrabbyConstants.IntakeConstants.intakeSpeed)
        );
        addRequirements(shooter, wrist, intake, indexer, drive);
    }
}
