package frc.robot.commands;

import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.TargetingCalculator;

public class PrepAutoShot extends ProxyCommand {
    public PrepAutoShot(NFRSwerveDrive drive, TargetCamera camera, Supplier<Pose2d> poseSupplier, Shooter shooter, TargetingCalculator speedCalculator,
        WristJoint wrist, TargetingCalculator wristCalculator, double cameraHeight, Rotation2d cameraPitch, Rotation2d tolerance, double speedTolerance)
    {
        super(
            new ParallelCommandGroup(
                new PathPlannerAlignToGoal(drive, camera, poseSupplier),
                new RampShooterContinuous(shooter, () -> speedCalculator.getValueForDistance(
                    camera.getDistanceToSpeaker(cameraHeight, cameraPitch).orElse(0.0))),
                new NFRWristContinuousAngle(wrist, () -> Rotation2d.fromRadians(
                    wristCalculator.getValueForDistance(camera.getDistanceToSpeaker(cameraHeight, cameraPitch).orElse(0.0))))
            ).until(
                () -> camera.getSpeakerTag().isPresent() && camera.getSpeakerTag().get().yaw() <= tolerance.getRadians() &&
                shooter.isAtSpeed(speedTolerance))
        );
    }
}
