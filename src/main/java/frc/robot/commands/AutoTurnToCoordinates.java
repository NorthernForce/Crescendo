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
        return Math.abs(MathUtil.inputModulus(targetRotation - drive.getRotation().getDegrees(), -180, 180) + allianceOffset) <= tolerance.getDegrees();
    }
}
