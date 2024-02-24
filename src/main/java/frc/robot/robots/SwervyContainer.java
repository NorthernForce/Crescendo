package frc.robot.robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.motors.NFRTalonFX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;
import frc.robot.commands.OrchestraCommand;
import frc.robot.commands.auto.Autos;
import frc.robot.constants.SwervyConstants;
import frc.robot.dashboard.Dashboard;
import frc.robot.dashboard.SwervyDashboard;
import frc.robot.commands.FollowNote;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.Xavier;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;

public class SwervyContainer implements RobotContainer
{
    protected final SwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final NFRSwerveModuleSetState[] setStateCommandsVelocity;
    protected final OrangePi orangePi;
    protected final Xavier xavier;
    protected final Field2d field;
    protected final TargetCamera aprilTagCamera;
    protected final PoseSupplier aprilTagSupplier;
    protected final Notifier flushNotifier;
    protected final SwervyMap map;
    protected final SwervyDashboard dashboard;
    public SwervyContainer()
    {
        map = new SwervyMap();
        drive = new SwerveDrive(SwervyConstants.DriveConstants.config, map.modules, SwervyConstants.DriveConstants.offsets, map.gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(map.modules[0], 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 0, false)
        };
        setStateCommandsVelocity = new NFRSwerveModuleSetState[] {  //used when setting a velocity in m/s
            new NFRSwerveModuleSetState(map.modules[0], 1, 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 1, 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 1, 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 1, 0, false)
        };
        orangePi = new OrangePi(SwervyConstants.OrangePiConstants.config);
        xavier = new Xavier(SwervyConstants.XavierConstants.config);
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive).ignoringDisable(true));
        Shuffleboard.getTab("General").addBoolean("Orange Pi Connected", orangePi::isConnected);
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", xavier::isConnected);
        Shuffleboard.getTab("General").addFloat("Note Radian", xavier::getYawRadians);
        field = new Field2d();
        Shuffleboard.getTab("General").add("Field", field);
        Shuffleboard.getTab("General").add("Crab Rave",
            new OrchestraCommand("crab-rave.chrp", List.of(
                (NFRTalonFX)map.modules[0].getDriveController(),
                (NFRTalonFX)map.modules[0].getTurnController(),
                (NFRTalonFX)map.modules[1].getDriveController(),
                (NFRTalonFX)map.modules[1].getTurnController(),
                (NFRTalonFX)map.modules[2].getDriveController(),
                (NFRTalonFX)map.modules[2].getTurnController(),
                (NFRTalonFX)map.modules[3].getDriveController(),
                (NFRTalonFX)map.modules[3].getTurnController()), drive, map.modules[0], map.modules[1], map.modules[2], map.modules[3])
                .ignoringDisable(true));
        Shuffleboard.getTab("General").add("The Office",
            new OrchestraCommand("the-office.chrp", List.of(
                (NFRTalonFX)map.modules[0].getDriveController(),
                (NFRTalonFX)map.modules[0].getTurnController(),
                (NFRTalonFX)map.modules[1].getDriveController(),
                (NFRTalonFX)map.modules[1].getTurnController(),
                (NFRTalonFX)map.modules[2].getDriveController(),
                (NFRTalonFX)map.modules[2].getTurnController(),
                (NFRTalonFX)map.modules[3].getDriveController(),
                (NFRTalonFX)map.modules[3].getTurnController()), drive, map.modules[0], map.modules[1], map.modules[2], map.modules[3])
                .ignoringDisable(true));
        aprilTagCamera = orangePi.new TargetCamera("apriltag_camera");
        aprilTagSupplier = orangePi.new PoseSupplier("apriltag_camera", estimate -> {
            drive.addVisionEstimate(estimate.getSecond(), estimate.getFirst());
        });
        flushNotifier = new Notifier(() -> {NetworkTableInstance.getDefault().flush();});
        flushNotifier.startPeriodic(0.01);
        CameraServer.startAutomaticCapture();
        dashboard = new SwervyDashboard();
        dashboard.register(orangePi);
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
            new JoystickButton(driverController, XboxController.Button.kA.value)
                .whileTrue(new FollowNote(xavier, drive, setStateCommands,
                    () -> -MathUtil.applyDeadband(driverController.getLeftX(), 0.1, 1), true));
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
            new JoystickButton(driverHID, XboxController.Button.kA.value)
                .whileTrue(new FollowNote(xavier, drive, setStateCommands,
                    () -> -MathUtil.applyDeadband(driverHID.getRawAxis(0), 0.1, 1), true));
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
        drive.resetPose(pose);
        orangePi.setGlobalPose(pose);
    }
    @Override
    public void periodic()
    {
        orangePi.setOdometry(drive.getChassisSpeeds());
        field.setRobotPose(orangePi.getPose());
    }
    @Override
    public List<AutonomousRoutine> getAutonomousRoutines() {
        ArrayList<AutonomousRoutine> routines = new ArrayList<>();
        routines.add(new AutonomousRoutine("Do Nothing", Pose2d::new, Commands.none()));
        routines.addAll(Autos.getRoutines(drive, setStateCommandsVelocity, drive::getEstimatedPose,
            SwervyConstants.DriveConstants.holonomicDriveController));
        return routines;
    }
    @Override
    public Dashboard getDashboard()
    {
        return dashboard;
    }
}
