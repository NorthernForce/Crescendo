package frc.robot.commands;

import java.util.function.DoubleSupplier;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.FieldConstants;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.OrangePi.TargetCamera;

public class PositionToShoot extends SequentialCommandGroup
{
    public PositionToShoot(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PIDController controller,
        DoubleSupplier xSupplier, DoubleSupplier ySupplier, OrangePi orangePi, TargetCamera apriltagCamera, boolean optimize, boolean fieldRelative)
    {
        addCommands(
            new TurnToCoordinates(drive, setStateCommands, controller, xSupplier, ySupplier, orangePi::getPose, () -> {
                if (DriverStation.getAlliance().orElse(Alliance.Red) == DriverStation.Alliance.Red)
                {
                    return FieldConstants.SpeakerPositions.redSpeaker;
                }
                else
                {
                    return FieldConstants.SpeakerPositions.blueSpeaker;
                }
            }, optimize, fieldRelative).until(() -> apriltagCamera.getSpeakerTag().isPresent()),
            new TurnToTarget(drive, setStateCommands, controller, xSupplier, ySupplier, ySupplier, apriltagCamera::getSpeakerTag, optimize, fieldRelative)
        );
    }
}
