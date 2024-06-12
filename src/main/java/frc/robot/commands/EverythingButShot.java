package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NFRPhotonCamera;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;

public class EverythingButShot extends SequentialCommandGroup {
    public EverythingButShot(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Shooter shooter,
            NFRPhotonCamera orangePi, WristJoint wristJoint, DoubleSupplier topSpeed,
            DoubleSupplier bottomSpeed, Supplier<Rotation2d> wristSupplier) {
        addCommands(
                new ParallelDeadlineGroup(
                        Commands.waitUntil(() -> drive.getSpeed() < 0.5
                                && shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance) &&
                                Math.abs(wristJoint.getRotation().getDegrees() - wristSupplier.get().getDegrees()) < 2),
                        new AutoPrepShot(drive, orangePi, shooter, wristJoint, topSpeed, bottomSpeed, wristSupplier)),
                new TurnToTarget2(drive, setStateCommands, CrabbyConstants.DriveConstants.controller2,
                        orangePi::getSpeakerTagYaw, true));
        addRequirements(shooter, wristJoint);
    }
}
