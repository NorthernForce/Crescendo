package frc.robot.robots;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;

import org.northernforce.motors.NFRTalonFX;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;
import org.northernforce.commands.NFRRotatingArmJointWithJoystick;
import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.commands.NFRSwerveDriveStop;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.FollowNote;
import frc.robot.commands.NFRWristContinuous;
import frc.robot.commands.OrchestraCommand;
import frc.robot.commands.PurgeIntake;
import frc.robot.commands.RumbleController;
import frc.robot.commands.RunIntake;
import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.dashboard.Dashboard;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.subsystems.Xavier;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;

public class CrabbyContainer implements RobotContainer
{
    protected final SwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;

    protected final OrangePi orangePi;
    protected final Xavier xavier;
    protected final TargetCamera aprilTagCamera;
    protected final PoseSupplier aprilTagSupplier;
    protected final WristJoint wristJoint;
    protected final CrabbyMap map;
    protected final CrabbyDashboard dashboard;
    protected final Intake intake;
    protected boolean manualWrist;
    public CrabbyContainer()
    {
        manualWrist = false;
        map = new CrabbyMap();
        wristJoint = new WristJoint(map.wristSparkMax, CrabbyConstants.Wrist.wristConfig);
        
        drive = new SwerveDrive(new NFRSwerveDriveConfiguration("drive"), map.modules, CrabbyConstants.Drive.offsets, map.gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(map.modules[0], 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 0, false)
        };

        orangePi = new OrangePi(new OrangePiConfiguration("orange pi", "xavier"));
        xavier = new Xavier(CrabbyConstants.XavierConstants.config);
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive).ignoringDisable(true));
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        Shuffleboard.getTab("General").addDouble("Degrees of Wrist", () -> wristJoint.getRotation().getDegrees());
        Shuffleboard.getTab("General").addBoolean("Manual Wrist Positioning", () -> manualWrist);
        // Shuffleboard.getTab("General").add("Calibrate Wrist", new NFRResetWristCommand(wristJoint).ignoringDisable(true));
        SendableChooser<String> musicChooser = new SendableChooser<>();
        musicChooser.setDefaultOption("Mr. Blue Sky", "blue-sky.chrp");
        musicChooser.addOption("Crab Rave", "crab-rave.chrp");
        musicChooser.addOption("The Office", "the-office.chrp");
        Shuffleboard.getTab("General").add("Music Selector", musicChooser);
        Shuffleboard.getTab("General").add("Play Music", new ProxyCommand(() -> {
            return new OrchestraCommand(musicChooser.getSelected(), List.of(
                (NFRTalonFX)map.modules[0].getDriveController(),
                (NFRTalonFX)map.modules[0].getTurnController(),
                (NFRTalonFX)map.modules[1].getDriveController(),
                (NFRTalonFX)map.modules[1].getTurnController(),
                (NFRTalonFX)map.modules[2].getDriveController(),
                (NFRTalonFX)map.modules[2].getTurnController(),
                (NFRTalonFX)map.modules[3].getDriveController(),
                (NFRTalonFX)map.modules[3].getTurnController()), drive, map.modules[0], map.modules[1], map.modules[2], map.modules[3])
                .ignoringDisable(true);
        }));
        aprilTagCamera = orangePi.new TargetCamera("apriltag_camera");
        aprilTagSupplier = orangePi.new PoseSupplier("apriltag_camera", estimate -> {});
        dashboard = new CrabbyDashboard();
        intake = new Intake(map.intakeMotor, map.intakeBeamBreak);
        dashboard.register(orangePi);
        Shuffleboard.getTab("General").addBoolean("DIO Port 7", intake::getBeamBreakState);
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
            new JoystickButton(driverHID, XboxController.Button.kA.value)
                .whileTrue(new FollowNote(xavier, drive, setStateCommands,
                    () -> -MathUtil.applyDeadband(driverController.getLeftX(), 0.1, 1), true));
            new Trigger(() -> driverController.getLeftTriggerAxis() > 0.4)
                .toggleOnTrue(new RunIntake(intake, CrabbyConstants.IntakeConstants.intakeSpeed));
            //new Trigger(() -> intake.getBeamBreak().beamBroken())
            //    .onTrue(new RumbleController(driverController, 0.5, 0.5));
            new JoystickButton(driverController, XboxController.Button.kBack.value)
                .whileTrue(new PurgeIntake(intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed));
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
        if (manipulatorHID instanceof XboxController)
        {
            XboxController manipulatorController = (XboxController)manipulatorHID;
            new Trigger(() -> manipulatorController.getLeftTriggerAxis() > 0.4)
                .whileTrue(new RunIntake(intake, CrabbyConstants.IntakeConstants.intakeSpeed));
            new Trigger(() -> intake.getBeamBreak().beamBroken())
                .onTrue(new RumbleController(manipulatorController, 0.5, 0.5));
            new JoystickButton(manipulatorController, XboxController.Button.kX.value)
                .whileTrue(new PurgeIntake(intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed));
            new JoystickButton(manipulatorController, XboxController.Button.kB.value)
                .toggleOnTrue(new NFRRotatingArmJointWithJoystick(wristJoint, () -> -MathUtil.applyDeadband(manipulatorController.getLeftY(), 0.1, 1)).alongWith(Commands.runOnce(() -> manualWrist = true)));
            new JoystickButton(manipulatorController, XboxController.Button.kA.value)
                .toggleOnFalse(new NFRWristContinuous(wristJoint, () -> Optional.of(0.25)).alongWith(Commands.runOnce(() -> manualWrist = false)));

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
        orangePi.setGlobalPose(pose);
        drive.resetPose(pose);
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
