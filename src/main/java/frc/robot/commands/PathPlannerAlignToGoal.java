package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.FieldConstants;
import frc.robot.subsystems.OrangePi.TargetCamera;

public class PathPlannerAlignToGoal extends Command
{
    protected final NFRSwerveDrive drive;
    protected final TargetCamera camera;
    protected final Supplier<Pose2d> poseSupplier;
    public PathPlannerAlignToGoal(NFRSwerveDrive drive, TargetCamera camera, Supplier<Pose2d> poseSupplier)
    {
        this.drive = drive;
        this.camera = camera;
        this.poseSupplier = poseSupplier;
    }
    @Override
    public void initialize()
    {
        PPHolonomicDriveController.setRotationTargetOverride(() -> {
            var detection = camera.getSpeakerTag();
            if (detection.isPresent())
            {
                return Optional.of(drive.getRotation().plus(Rotation2d.fromRadians(camera.getSpeakerTag().get().yaw())));
            }
            else
            {
                return Optional.of(poseSupplier.get().getTranslation().minus(
                    DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? FieldConstants.SpeakerConstants.redSpeaker
                        : FieldConstants.SpeakerConstants.blueSpeaker).getAngle());
            }
        });
    }
    @Override
    public void end(boolean isFinished)
    {
        PPHolonomicDriveController.setRotationTargetOverride(null);
    }
}
