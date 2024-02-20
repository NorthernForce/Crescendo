package frc.robot.robots;

import java.util.List;
import java.util.Map;

import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;

import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint.NFRRotatingArmJointConfiguration;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.gyros.NFRPigeon2;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyContainer implements RobotContainer
{
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final SwerveDrive drive;
    protected final Field2d field;
    protected final OrangePi orangePi;
    protected final TargetCamera aprilTagCamera, noteDetectorCamera;
    protected final PoseSupplier aprilTagSupplier;
    protected final NFRPigeon2 gyro;
    protected final NFRRotatingArmJoint wristJoint;
    public CrabbyContainer()
    {
        NFRSparkMax wristController = new NFRSparkMax(MotorType.kBrushless, 14);
        NFRRotatingArmJointConfiguration wristConfig = new NFRRotatingArmJointConfiguration("wristConfig");
        wristJoint = new NFRRotatingArmJoint(wristConfig, wristController, java.util.Optional.empty());
        gyro = new NFRPigeon2(13);
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
        drive = new SwerveDrive(new NFRSwerveDriveConfiguration("drive"), modules, offsets, gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(modules[0], 0, false),
            new NFRSwerveModuleSetState(modules[1], 0, false),
            new NFRSwerveModuleSetState(modules[2], 0, false),
            new NFRSwerveModuleSetState(modules[3], 0, false)
        };
        field = new Field2d();
        orangePi = new OrangePi(new OrangePiConfiguration("orange pi", "xavier"));
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        noteDetectorCamera = orangePi.new TargetCamera("usb_cam2");
        aprilTagCamera = orangePi.new TargetCamera("usb_cam1");
        aprilTagSupplier = orangePi.new PoseSupplier("usb_cam1", estimate -> {});
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
                
        }
        else
        {
            drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(drive, setStateCommands,
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(1), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(0), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(5), 0.1, 1), true, true));
           
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
        return Map.of("Simple Starting Location", new Pose2d(5, 5, Rotation2d.fromDegrees(0)));
    }
    @Override
    public Pair<String, Command> getDefaultAutonomous()
    {
        return Pair.of("Do Nothing", Commands.none());
    }
    @Override
    public void setInitialPose(Pose2d pose)
    {
    }
    @Override
    public void periodic()
    {
    }
    @Override
    public List<AutonomousRoutine> getAutonomousRoutines() {
        return List.of(new AutonomousRoutine("Do nothing", new Pose2d(), Commands.none()));
    }
}
