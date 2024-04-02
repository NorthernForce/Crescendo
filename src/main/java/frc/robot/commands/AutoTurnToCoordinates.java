package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRPhotonCamera;

public class AutoTurnToCoordinates extends Command {
    protected final NFRSwerveDrive drive;
    protected final Supplier<Translation2d> targetCoordinates;
    protected final Rotation2d tolerance;
    protected final NFRPhotonCamera camera;
    public AutoTurnToCoordinates(NFRSwerveDrive drive, Supplier<Translation2d> targetCoordinates, Rotation2d tolerance, NFRPhotonCamera camera)
    {
        this.drive = drive;
        this.targetCoordinates = targetCoordinates;
        this.tolerance = tolerance;
        this.camera = camera;
    }
    @Override
    public void initialize()
    {
        PPHolonomicDriveController.setRotationTargetOverride(() -> {
            var speakerTagYaw = camera.getSpeakerTagYaw();
            if (speakerTagYaw.isPresent()) {
                return Optional.of(drive.getRotation().plus(speakerTagYaw.get()));
            }
            return Optional.of(targetCoordinates.get().minus(drive.getEstimatedPose().getTranslation()).getAngle()
                .plus(Rotation2d.fromDegrees(180)));
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
        var targetRotation = targetCoordinates.get().minus(drive.getEstimatedPose().getTranslation()).getAngle()
            .plus(Rotation2d.fromDegrees(180)).getDegrees();
        var allianceOffset = DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? 
            180 : 0;
        return Math.abs(MathUtil.inputModulus(targetRotation - drive.getRotation().getDegrees() + allianceOffset, -180, 180)) <= tolerance.getDegrees();
    }
}
