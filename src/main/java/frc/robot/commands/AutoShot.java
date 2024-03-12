package frc.robot.commands;

import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.FieldConstants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.TargetingCalculator;

public class AutoShot extends SequentialCommandGroup {
    public AutoShot(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, TargetCamera camera, Shooter shooter, Intake intake,
        WristJoint wrist, Supplier<Pose2d> poseSupplier, PIDController controller, boolean optimize, boolean fieldRelative, Rotation2d tolerance,
        TargetingCalculator wristCalculator, TargetingCalculator speedCalculator, double cameraHeight, Rotation2d cameraPitch, double speedTolerance,
        double intakeSpeed, Rotation2d wristTolerance, double clearanceTime)
    {
        addCommands(
            new TurnToCoordinates(drive, setStateCommands, poseSupplier, () -> {
                if (DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red) 
                {
                    return FieldConstants.SpeakerConstants.redSpeaker;
                }
                else
                {
                    return FieldConstants.SpeakerConstants.blueSpeaker;
                }
            }, controller, optimize, fieldRelative).until(() -> camera.getSpeakerTag().isPresent()),
            new ParallelCommandGroup(
                new TurnToTarget(drive, setStateCommands, controller, () -> 0, () -> 0, () -> 0, camera::getSpeakerTag, optimize, fieldRelative),
                new RampShooterContinuous(shooter,
                    () -> speedCalculator.getValueForDistance(camera.getDistanceToSpeaker(cameraHeight, cameraPitch).orElse(0.0))),
                new NFRWristContinuousAngle(wrist,
                    () -> Rotation2d.fromRadians(wristCalculator.getValueForDistance(camera.getDistanceToSpeaker(cameraHeight, cameraPitch).orElse(0.0))))
            ).until(
                () -> camera.getSpeakerTag().isPresent() && camera.getSpeakerTag().get().yaw() <= tolerance.getRadians() &&
                shooter.isAtSpeed(speedTolerance)),
            new ShootIntake(intake, intakeSpeed),
            Commands.waitSeconds(clearanceTime)
        );
        addRequirements(shooter, intake, wrist, drive);
    }
}
