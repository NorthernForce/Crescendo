package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.FieldConstants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class AutoPrepShot extends ParallelCommandGroup {
    public AutoPrepShot(NFRSwerveDrive drive, Shooter shooter, WristJoint wrist, DoubleSupplier topSpeed, DoubleSupplier bottomSpeed, Supplier<Rotation2d> wristSupplier)
    {
        addCommands(
            new AutoTurnToCoordinates(drive, () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? FieldConstants.SpeakerConstants.redSpeaker : FieldConstants.SpeakerConstants.blueSpeaker,
                Rotation2d.fromDegrees(5)),
            new NFRWristContinuousAngle(wrist, wristSupplier),
            new RampShooterWithDifferential(shooter, topSpeed, bottomSpeed)
        );
    }
}
