package frc.robot.robots;

import java.util.Map;
import java.util.Optional;

import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.encoders.NFRCANCoder;
import org.northernforce.gyros.NFRNavX;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRTalonFX;
import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule.Mk3SwerveConstants;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule.NFRSwerveModuleConfiguration;
import org.northernforce.util.NFRRobotContainer;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class SwervyContainer implements NFRRobotContainer
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    /**
     * Creates a traditional Mk3 swerve module with standard gearing and two falcon 500s, as well as a cancoder.
     * @param name the name of the module
     * @param driveID the id of the drive motor.
     * @param turnID the id of the turn motor.
     * @param cancoderID the id of the cancoder.
     * @param invertDrive whether to invert the drive motor.
     * @return
     */
    public static NFRSwerveModule createMk3Slow(String name, int driveID, int turnID, int cancoderID, boolean invertDrive, String canbus)
    {
        NFRSwerveModuleConfiguration config = new NFRSwerveModuleConfiguration(name)
            .withGearRatios(Mk3SwerveConstants.kDriveGearRatioSlow, Mk3SwerveConstants.kTurnGearRatio)
            .withGearboxes(DCMotor.getFalcon500(1), DCMotor.getFalcon500(1))
            .withMOIs(1.2, 1.2)
            .withMaxSpeed(Mk3SwerveConstants.kDriveMaxSpeed);
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 60;
        driveConfig.CurrentLimits.SupplyCurrentThreshold = 90;
        driveConfig.CurrentLimits.SupplyTimeThreshold = 0.5;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.Slot0.kP = Mk3SwerveConstants.kDriveP;
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.MotorOutput.DutyCycleNeutralDeadband = 0.1;
        NFRTalonFX driveMotor = new NFRTalonFX(canbus, driveConfig, driveID);
        driveMotor.getSelectedEncoder().setConversionFactor(Mk3SwerveConstants.kWheelCircumference /
            Mk3SwerveConstants.kDriveGearRatioSlow);
        driveMotor.setInverted(invertDrive);
        TalonFXConfiguration turnConfig = new TalonFXConfiguration();
        turnConfig.CurrentLimits.SupplyCurrentLimit = 60;
        turnConfig.CurrentLimits.SupplyCurrentThreshold = 90;
        turnConfig.CurrentLimits.SupplyTimeThreshold = 0.5;
        turnConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        turnConfig.Slot0.kP = Mk3SwerveConstants.kTurnP;
        turnConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.4;
        turnConfig.ClosedLoopGeneral.ContinuousWrap = true;
        NFRTalonFX turnMotor = new NFRTalonFX(canbus, turnConfig, turnID);
        NFRCANCoder cancoder = new NFRCANCoder(canbus, cancoderID);
        try
        {
            turnMotor.setSelectedEncoder(cancoder);
        }
        catch (MotorEncoderMismatchException e)
        {
            e.printStackTrace();
        }
        return new NFRSwerveModule(config, driveMotor, turnMotor, Optional.empty());
    }
    public SwervyContainer()
    {
        NFRSwerveModule[] modules = new NFRSwerveModule[] {
            createMk3Slow("Front Left", 1, 5, 9, false, "drive"),
            createMk3Slow("Front Right", 2, 6, 10, true, "drive"),
            createMk3Slow("Back Left", 3, 7, 11, false, "drive"),
            createMk3Slow("Back Right", 4, 8, 12, true, "drive")
        };
        Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.581025, 0.581025),
            new Translation2d(0.581025, -0.581025),
            new Translation2d(-0.581025, 0.581025),
            new Translation2d(-0.581025, -0.581025)
        };
        NFRNavX gyro = new NFRNavX();
        gyro.reset();
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
