package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoTurnToCoordinates extends Command {
    protected final NFRSwerveDrive drive;
    protected final Supplier<Translation2d> targetCoordinates;
    protected final Rotation2d tolerance;
    public AutoTurnToCoordinates(NFRSwerveDrive drive, Supplier<Translation2d> targetCoordinates, Rotation2d tolerance)
    {
        this.drive = drive;
        this.targetCoordinates = targetCoordinates;
        this.tolerance = tolerance;
    }
    @Override
    public void initialize()
    {
        PPHolonomicDriveController.setRotationTargetOverride(() -> {
            return Optional.of(targetCoordinates.get().minus(drive.getEstimatedPose().getTranslation()).getAngle());
        });
    }
    @Override
    public void end(boolean interrupted)
    {
        PPHolonomicDriveController.setRotationTargetOverride(null);
    }
    @Override
    public boolean isFinished()
    {
        return Math.abs(drive.getRotation().minus(targetCoordinates.get().minus(drive.getEstimatedPose().getTranslation()).getAngle()).getDegrees()) <= tolerance.getDegrees();
    }
}
