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
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.utils.SwerveModuleHelpers;
import frc.robot.gyros.NFRPigeon2;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.OrangePi.TargetCamera;

public class SwervyContainer implements NFRRobotContainer
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final OrangePi orangePi;
    protected final Field2d field;
    protected final TargetCamera aprilTagCamera, noteDetectorCamera;
    protected final NFRPigeon2 gyro;
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
        gyro = new NFRPigeon2(13);
        drive = new NFRSwerveDrive(new NFRSwerveDriveConfiguration("drive"), modules, offsets, gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(modules[0], 0, false),
            new NFRSwerveModuleSetState(modules[1], 0, false),
            new NFRSwerveModuleSetState(modules[2], 0, false),
            new NFRSwerveModuleSetState(modules[3], 0, false)
        };
        orangePi = new OrangePi(new OrangePiConfiguration("orange pi", "xavier"));
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive));
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        field = new Field2d();
        Shuffleboard.getTab("General").add("Field", field);
        noteDetectorCamera = orangePi.new TargetCamera("usb_cam2");
        aprilTagCamera = orangePi.new TargetCamera("usb_cam1");
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
        else
        {
            drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(drive, setStateCommands,
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(1), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(0), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(5), 0.1, 1), true, true));
            new JoystickButton(driverHID, XboxController.Button.kB.value)
                .onTrue(Commands.runOnce(drive::clearRotation, drive));
            new JoystickButton(driverHID, XboxController.Button.kY.value)
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
        orangePi.sendGlobalSetPose(pose, Timer.getFPGATimestamp());
    }
    @Override
    public void periodic()
    {
        var chassisSpeeds = drive.getChassisSpeeds();
        orangePi.setOdometry(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond,
            Rotation2d.fromRadians(chassisSpeeds.omegaRadiansPerSecond), Timer.getFPGATimestamp());
        orangePi.setIMU(Rotation2d.fromDegrees(gyro.getRate()), Timer.getFPGATimestamp());
        field.setRobotPose(orangePi.getPose());
    }
}