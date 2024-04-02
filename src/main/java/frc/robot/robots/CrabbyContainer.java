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
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.AddDataToTargetingCalculator;
import frc.robot.commands.Autos;
import frc.robot.commands.CloseShot;
import frc.robot.commands.OrchestraCommand;
import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.dashboard.Dashboard;
import frc.robot.oi.CrabbyOI;
import frc.robot.oi.DefaultCrabbyOI;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.commands.RampShooterContinuous;
import frc.robot.subsystems.NFRPhotonCamera;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
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

    public final NFRPhotonCamera orangePi;
    public final Xavier xavier;
    public final WristJoint wristJoint;
    public final CrabbyMap map;
    public final CrabbyDashboard dashboard;
    public final Intake intake;
    public final Indexer indexer;
    public final Shooter shooter;
    public boolean manualWrist;
    public double lastRecordedDistance = 0;
    public final GenericEntry shooterSpeed;
    public final GenericEntry topRollerChange;
    public final TargetingCalculator bottomSpeedCalculator;
    public final TargetingCalculator topSpeedCalculator;
    public final TargetingCalculator angleCalculator;
    public CrabbyOI oi;
    public final Climber climber;
    public CrabbyContainer()
    {
        
        dashboard = new CrabbyDashboard();

        map = new CrabbyMap();
        intake = new Intake(map.intakeMotor);

        climber = new Climber(map.climberMotor);
        Shuffleboard.getTab("General").addDouble("Climber Position", () -> climber.getPosition());

        indexer = new Indexer(map.indexerMotor, map.indexerBeamBreak);

        angleCalculator = new InterpolatedTargetingCalculator("/home/lvuser/angleData.csv");
        wristJoint = new WristJoint(map.wristSparkMax, CrabbyConstants.WristConstants.wristConfig, dashboard);
        Shuffleboard.getTab("Developer").add("Add Wrist Data", new AddDataToTargetingCalculator(angleCalculator, () -> lastRecordedDistance, 
            () -> wristJoint.getRotation().getRadians()).ignoringDisable(true));
        Shuffleboard.getTab("General").addDouble("Degrees of Wrist", () -> wristJoint.getRotation().getDegrees());
        manualWrist = false;
        Shuffleboard.getTab("General").addBoolean("Manual Wrist Positioning", () -> manualWrist);
        Shuffleboard.getTab("General").addDouble("Wrist Target", () -> Math.toDegrees(angleCalculator.getValueForDistance(lastRecordedDistance)));
        // Shuffleboard.getTab("General").add("Calibrate Wrist", new NFRResetWristCommand(wristJoint).ignoringDisable(true));
        
        drive = new SwerveDrive(CrabbyConstants.DriveConstants.config, map.modules, CrabbyConstants.DriveConstants.offsets, map.gyro, dashboard);
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

        orangePi = new NFRPhotonCamera(CrabbyConstants.OrangePiConstants.config);
        dashboard.register(orangePi);
        Shuffleboard.getTab("Developer").addDouble("Distance", () -> lastRecordedDistance);

        xavier = new Xavier(CrabbyConstants.XavierConstants.config);
        PortForwarder.add(5806, "10.1.72.11", 5800);
        
        shooter = new Shooter(map.shooterMotorTop, map.shooterMotorBottom, dashboard);
        Shuffleboard.getTab("General").addDouble("Top Shooter", shooter::getTopMotorVelocity);
        Shuffleboard.getTab("General").addDouble("Bottom Shooter", shooter::getBottomMotorVelocity);
        Shuffleboard.getTab("General").addBoolean("At Speed", () -> shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance));
        Shuffleboard.getTab("General").addDouble("Index Current", indexer::getMotorCurrent);
        Shuffleboard.getTab("General").addBoolean("Climber Switch", () -> climber.isDown());

        Shuffleboard.getTab("General").addDouble("Intake Current", intake::getMotorCurrent);

        shooter.setDefaultCommand(new RampShooterContinuous(shooter, () -> indexer.getBeamBreak().beamBroken() ? 25 : 0));
        shooterSpeed = Shuffleboard.getTab("Developer").add("Shooter Speed", 30).getEntry();
        topRollerChange = Shuffleboard.getTab("Developer").add("Top Roller Change", 0).getEntry();
        bottomSpeedCalculator = new InterpolatedTargetingCalculator("/home/lvuser/bottomSpeedData.csv");
        topSpeedCalculator = new InterpolatedTargetingCalculator("/home/lvuser/topSpeedData.csv");
        Shuffleboard.getTab("Developer").add("Add Top Shooter Data", 
            new AddDataToTargetingCalculator(topSpeedCalculator, () -> lastRecordedDistance, () -> shooterSpeed.getDouble(30) + topRollerChange.getDouble(0)).ignoringDisable(true)
        );
        Shuffleboard.getTab("Developer").add("Add Bottom Shooter Data", 
            new AddDataToTargetingCalculator(bottomSpeedCalculator, () -> lastRecordedDistance, () -> shooterSpeed.getDouble(30)).ignoringDisable(true)
        );
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
        Shuffleboard.getTab("Developer").addDouble("Speaker Yaw", () -> orangePi.getSpeakerTagYaw().orElse(Rotation2d.fromDegrees(100)).getDegrees());
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
        drive.resetPose(pose);
    }
    @Override
    public void periodic()
    {
        var distance =
            orangePi.getDistanceToSpeaker(CrabbyConstants.OrangePiConstants.cameraHeight, CrabbyConstants.OrangePiConstants.cameraPitch);
        if (distance.isPresent())
        {
            lastRecordedDistance = distance.get();
        }
        // var estimatedPose = orangePi.getPose();
        // if (estimatedPose.isPresent())
        // {
        //     drive.addVisionEstimate(estimatedPose.get().timestampSeconds, estimatedPose.get().estimatedPose.toPose2d());
        // }
        dashboard.periodic();
    }
    @Override
    public List<AutonomousRoutine> getAutonomousRoutines() {
        ArrayList<AutonomousRoutine> routines = new ArrayList<>();
        routines.add(new AutonomousRoutine("Do Nothing", Pose2d::new, Commands.none()));
        routines.add(new AutonomousRoutine("S1.SHOOT", () -> new Pose2d(new Translation2d(), DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? Rotation2d.fromDegrees(120) : Rotation2d.fromDegrees(60)),
            new CloseShot(shooter, wristJoint, indexer, intake)));
        routines.add(new AutonomousRoutine("S2.SHOOT", () -> new Pose2d(new Translation2d(), DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? Rotation2d.fromDegrees(180) : Rotation2d.fromDegrees(0)),
            new CloseShot(shooter, wristJoint, indexer, intake)));
        routines.add(new AutonomousRoutine("S3.SHOOT", () -> new Pose2d(new Translation2d(), DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? Rotation2d.fromDegrees(240) : Rotation2d.fromDegrees(-60)),
            new CloseShot(shooter, wristJoint, indexer, intake)));
        routines.addAll(Autos.getRoutines(drive, setStateCommands, drive::getEstimatedPose, pose -> {
                drive.resetPose(pose);
            },
            CrabbyConstants.DriveConstants.holonomicConfig, () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, intake,
            shooter, wristJoint, indexer, orangePi, () -> lastRecordedDistance, topSpeedCalculator, bottomSpeedCalculator, angleCalculator));
        return routines;
    }
    @Override
    public Dashboard getDashboard()
    {
        return dashboard;
    }
}
