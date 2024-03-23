package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.FieldConstants;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.NFRPhotonCamera;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;

public class AutoPrepShot extends ParallelDeadlineGroup {
    NFRSwerveDrive drive;
    Shooter shooter;
    public AutoPrepShot(SwerveDrive drive, NFRPhotonCamera orangePi, Shooter shooter, WristJoint wrist, DoubleSupplier topSpeed, DoubleSupplier bottomSpeed, Supplier<Rotation2d> wristSupplier)
    {
        super(
            new ParallelCommandGroup(
            Commands.waitUntil(() -> (drive.getSpeed() < 0.5
                && shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance)
                && Math.abs(drive.getChassisSpeeds().omegaRadiansPerSecond) < 0.15)),
            new AutoTurnToCoordinates(drive, () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? FieldConstants.SpeakerConstants.redSpeaker : FieldConstants.SpeakerConstants.blueSpeaker,
                Rotation2d.fromDegrees(20))
            ),
            new NFRWristContinuousAngle(wrist, wristSupplier),
            new RampShooterWithDifferential(shooter, topSpeed, bottomSpeed)
        );
    }
}
