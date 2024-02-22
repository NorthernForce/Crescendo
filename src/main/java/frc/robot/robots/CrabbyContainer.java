package frc.robot.robots;

import java.util.List;
import java.util.Map;

import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.dashboard.Dashboard;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;

public class CrabbyContainer implements RobotContainer
{
    protected final SwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;

    protected final OrangePi orangePi;
    protected final TargetCamera aprilTagCamera, noteDetectorCamera;
    protected final PoseSupplier aprilTagSupplier;
    protected final CrabbyMap map;
    protected final CrabbyDashboard dashboard;
    protected final Indexer indexer;
    protected final Intake intake;
    public CrabbyContainer()
    {
        map = new CrabbyMap();
        
        drive = new SwerveDrive(new NFRSwerveDriveConfiguration("drive"), map.modules, CrabbyConstants.Drive.offsets, map.gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(map.modules[0], 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 0, false)
        };

        orangePi = new OrangePi(new OrangePiConfiguration("orange pi", "xavier"));
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive).ignoringDisable(true));
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        noteDetectorCamera = orangePi.new TargetCamera("usb_cam2");
        aprilTagCamera = orangePi.new TargetCamera("usb_cam1");
        aprilTagSupplier = orangePi.new PoseSupplier("usb_cam1", estimate -> {});
        dashboard = new CrabbyDashboard();
        indexer = new Indexer(map.indexerMotor, map.indexerBeamBreak);
        intake = map.intake;
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
                .whileTrue(new NFRSwerveDriveStop(drive, setStateCommands, true));
        }
        else
        {
            drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(drive, setStateCommands,
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(1), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(0), 0.1, 1),
                () -> -MathUtil.applyDeadband(driverHID.getRawAxis(4), 0.1, 1), true, true));
            new JoystickButton(driverHID, XboxController.Button.kB.value)
                .onTrue(Commands.runOnce(drive::clearRotation, drive));
            new JoystickButton(driverHID, XboxController.Button.kY.value)
                .whileTrue(new NFRSwerveDriveStop(drive, setStateCommands, true));
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
        return List.of(new AutonomousRoutine("Do nothing", Pose2d::new, Commands.none()));
    }
    @Override
    public Dashboard getDashboard()
    {
        return dashboard;
    }
}
