package frc.robot.robots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.northernforce.commands.NFRSwerveModuleSetState;

import org.northernforce.motors.NFRTalonFX;
import org.northernforce.commands.NFRSwerveDriveCalibrate;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.AddDataToTargetingCalculator;
import frc.robot.commands.OrchestraCommand;
import frc.robot.commands.auto.Autos;
import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.dashboard.Dashboard;
import frc.robot.oi.CrabbyOI;
import frc.robot.oi.DefaultCrabbyOI;
import frc.robot.subsystems.Intake;
import frc.robot.commands.RestShooter;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.subsystems.Xavier;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;
import frc.robot.utils.TargetingCalculator;
import frc.robot.utils.InterpolatedTargetingCalculator;

public class CrabbyContainer implements RobotContainer
{
    public final SwerveDrive drive;
    public final NFRSwerveModuleSetState[] setStateCommands;
    public final NFRSwerveModuleSetState[] setStateCommandsVelocity;

    public final OrangePi orangePi;
    public final Xavier xavier;
    public final TargetCamera aprilTagCamera;
    public final PoseSupplier aprilTagSupplier;
    public final WristJoint wristJoint;
    public final CrabbyMap map;
    public final CrabbyDashboard dashboard;
    public final Intake intake;
    public final Shooter shooter;
    public boolean manualWrist;
    public double lastRecordedDistance = 0;
    public final GenericEntry shooterSpeed;
    public final TargetingCalculator speedCalculator;
    public final TargetingCalculator angleCalculator;
    public CrabbyOI oi;
    public CrabbyContainer()
    {
        dashboard = new CrabbyDashboard();

        map = new CrabbyMap();

        intake = new Intake(map.intakeMotor, map.intakeBeamBreak);

        wristJoint = new WristJoint(map.wristSparkMax, CrabbyConstants.WristConstants.wristConfig);
        // wristJoint.setDefaultCommand(new NFRWristContinuous(wristJoint, () -> Optional.of(0.0))
        //     .alongWith(Commands.runOnce(() -> manualWrist = false)));
        Shuffleboard.getTab("General").addDouble("Degrees of Wrist", () -> wristJoint.getRotation().getDegrees());
        manualWrist = false;
        Shuffleboard.getTab("General").addBoolean("Manual Wrist Positioning", () -> manualWrist);
        angleCalculator = new InterpolatedTargetingCalculator("/home/lvuser/angleData.csv");
        Shuffleboard.getTab("Developer").add("Add Wrist Data", new AddDataToTargetingCalculator(angleCalculator, () -> lastRecordedDistance, 
            () -> wristJoint.getRotation().getRadians()).ignoringDisable(true));
        // Shuffleboard.getTab("General").add("Calibrate Wrist", new NFRResetWristCommand(wristJoint).ignoringDisable(true));
        
        drive = new SwerveDrive(CrabbyConstants.DriveConstants.config, map.modules, CrabbyConstants.DriveConstants.offsets, map.gyro);
        setStateCommands = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(map.modules[0], 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 0, false)
        };
        setStateCommandsVelocity = new NFRSwerveModuleSetState[] {
            new NFRSwerveModuleSetState(map.modules[0], 0, 0, false),
            new NFRSwerveModuleSetState(map.modules[1], 0, 0, false),
            new NFRSwerveModuleSetState(map.modules[2], 0, 0, false),
            new NFRSwerveModuleSetState(map.modules[3], 0, 0, false)
        };
        Shuffleboard.getTab("General").add("Calibrate Swerve", new NFRSwerveDriveCalibrate(drive).ignoringDisable(true));

        orangePi = new OrangePi(CrabbyConstants.OrangePiConstants.config);
        aprilTagCamera = orangePi.new TargetCamera("apriltag_camera");
        aprilTagSupplier = orangePi.new PoseSupplier("apriltag_camera", estimate -> {});
        dashboard.register(orangePi);
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        Shuffleboard.getTab("General").addDouble("Distance",
            () ->
            {
                var distance =
                    aprilTagCamera.getDistanceToSpeaker(CrabbyConstants.OrangePiConstants.cameraHeight, CrabbyConstants.OrangePiConstants.cameraPitch);
                if (distance.isPresent())
                {
                    lastRecordedDistance = distance.get();
                }
                return lastRecordedDistance;
            });

        xavier = new Xavier(CrabbyConstants.XavierConstants.config);
        
        shooter = new Shooter(map.shooterMotorTop, map.shooterMotorBottom);
        shooter.setDefaultCommand(new RestShooter(shooter));
        shooterSpeed = Shuffleboard.getTab("Developer").add("Shooter Speed", 30).getEntry();
        speedCalculator = new InterpolatedTargetingCalculator("/home/lvuser/speedData.csv");
        Shuffleboard.getTab("Developer").add("Add Shooter Data", new AddDataToTargetingCalculator(speedCalculator, () -> lastRecordedDistance,
            () -> shooterSpeed.getDouble(30)).ignoringDisable(true));
        
        SendableChooser<String> musicChooser = new SendableChooser<>();
        musicChooser.setDefaultOption("Mr. Blue Sky", "blue-sky.chrp");
        musicChooser.addOption("Crab Rave", "crab-rave.chrp");
        musicChooser.addOption("The Office", "the-office.chrp");
        Shuffleboard.getTab("General").add("Music Selector", musicChooser);
        Shuffleboard.getTab("Developer").add("Add Wrist and Shooter Data", new AddDataToTargetingCalculator(speedCalculator, () -> 0, () -> shooterSpeed.getDouble(0)).ignoringDisable(true));
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
        
    }
    @Override
    @Deprecated
    public void bindOI(GenericHID driverHID, GenericHID manipulatorHID)
    {
    }
    @Override
    public void bindOI()
    {
        oi = new DefaultCrabbyOI();
        if (DriverStation.getJoystickIsXbox(0))
        {
            oi.bindDriverToXBoxController(this, new CommandXboxController(0));
        }
        else
        {
            oi.bindDriverToJoystick(this, new CommandGenericHID(0));
        }
        if (DriverStation.getJoystickIsXbox(1))
        {
            oi.bindManipulatorToXboxController(this, new CommandXboxController(1));
        }
        else
        {
            oi.bindManipulatorToJoystick(this, new CommandGenericHID(1));
        }
    }
    @Deprecated
    @Override
    public Map<String, Command> getAutonomousOptions()
    {
        return Map.of();
    }
    @Deprecated
    @Override
    public Map<String, Pose2d> getStartingLocations()
    {
        return Map.of("Simple Starting Location", new Pose2d(5, 5, Rotation2d.fromDegrees(0)));
    }
    @Deprecated
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
        orangePi.setOdometry(drive.getChassisSpeeds());
        orangePi.setIMU(drive.getRotation());
        dashboard.updateRobotPose(orangePi.getPose());
    }
    @Override
    public List<AutonomousRoutine> getAutonomousRoutines() {
        ArrayList<AutonomousRoutine> routines = new ArrayList<>();
        routines.add(new AutonomousRoutine("Do Nothing", Pose2d::new, Commands.none()));
        routines.addAll(Autos.getRoutines(drive, setStateCommands, drive::getEstimatedPose,
            CrabbyConstants.DriveConstants.holonomicDriveController, intake,
            CrabbyConstants.IntakeConstants.intakeSpeed, shooter, wristJoint));
        return routines;
    }
    @Override
    public Dashboard getDashboard()
    {
        return dashboard;
    }
}
