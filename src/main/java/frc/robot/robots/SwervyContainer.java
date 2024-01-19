package frc.robot.robots;

import java.util.Map;
import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;
import org.northernforce.util.NFRRobotContainer;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.utils.SwerveModuleHelpers;
import frc.robot.gyros.NFRPigeon2;

public class SwervyContainer implements NFRRobotContainer
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    public SwervyContainer()
    {
        NFRSwerveModule[] modules = new NFRSwerveModule[] {
            SwerveModuleHelpers.createMk3Slow("Front Left", 1, 5, 9, false, "drive"),
            SwerveModuleHelpers.createMk3Slow("Front Right", 2, 6, 10, true, "drive"),
            SwerveModuleHelpers.createMk3Slow("Back Left", 3, 7, 11, false, "drive"),
            SwerveModuleHelpers.createMk3Slow("Back Right", 4, 8, 12, true, "drive")
        };
        Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.581025, 0.581025),
            new Translation2d(0.581025, -0.581025),
            new Translation2d(-0.581025, 0.581025),
            new Translation2d(-0.581025, -0.581025)
        };
        NFRPigeon2 gyro = new NFRPigeon2(13);
        drive = new NFRSwerveDrive(new NFRSwerveDriveConfiguration("drive"), modules, offsets, gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(modules[0], 0, false),
            new NFRSwerveModuleSetState(modules[1], 0, false),
            new NFRSwerveModuleSetState(modules[2], 0, false),
            new NFRSwerveModuleSetState(modules[3], 0, false)
        };
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive));
    }
    @Override
    public void bindOI(GenericHID driverHID, GenericHID manipulatorHID)
    {
        if (driverHID instanceof XboxController)
        {
            XboxController driverController = (XboxController)driverHID;
            drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(drive, setStateCommands,
                () -> -MathUtil.applyDeadband(driverController.getLeftY(), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverController.getLeftX(), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverController.getRightX(), 0.1, 1), true, true));
            new JoystickButton(driverController, XboxController.Button.kB.value)
                .onTrue(Commands.runOnce(drive::clearRotation, drive));
            new JoystickButton(driverController, XboxController.Button.kY.value)
                .onTrue(new NFRSwerveDriveStop(drive, setStateCommands, true));
        }
    }
    @Override
    public Map<String, Command> getAutonomousOptions()
    {
        return Map.of();
    }
    @Override
    public Map<String, Pose2d> getStartingLocations()
    {
        return Map.of("Simple Starting Location", new Pose2d());
    }
    @Override
    public Pair<String, Command> getDefaultAutonomous()
    {
        return Pair.of("Do Nothing", Commands.none());
    }
    @Override
    public void setInitialPose(Pose2d pose)
    {
        drive.resetPose(pose);
    }
}
