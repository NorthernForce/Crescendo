package frc.robot.utils;

import java.util.Optional;

import org.northernforce.encoders.NFRCANCoder;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRTalonFX;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule.Mk3SwerveConstants;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule.NFRSwerveModuleConfiguration;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.swerve.SwerveModule;

public class SwerveModuleHelpers
{
    /**
     * Creates a traditional Mk3 swerve module with standard gearing and two falcon 500s, as well as a cancoder.
     * @param name the name of the module
     * @param driveID the id of the drive motor.
     * @param turnID the id of the turn motor.
     * @param cancoderID the id of the cancoder.
     * @param invertDrive whether to invert the drive motor.
     * @param canbus the name of the canbus
     * @return a new NFRSwerveModule
     */
    public static SwerveModule createMk3Slow(String name, int driveID, int turnID, int cancoderID, boolean invertDrive, String canbus)
    {
        NFRSwerveModuleConfiguration config = new NFRSwerveModuleConfiguration(name)
            .withGearRatios(Mk3SwerveConstants.kDriveGearRatioSlow, Mk3SwerveConstants.kTurnGearRatio)
            .withGearboxes(DCMotor.getFalcon500(1), DCMotor.getFalcon500(1))
            .withMOIs(1.2, 1.2)
            .withMaxSpeed(Mk3SwerveConstants.kDriveMaxSpeed);
        
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 40;
        driveConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        driveConfig.CurrentLimits.SupplyTimeThreshold = 0.5;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.Audio.AllowMusicDurDisable = true;
        driveConfig.Slot0.kP = Mk3SwerveConstants.kDriveP;
        driveConfig.Slot1.kS = 0.01;
        driveConfig.Slot1.kV = 0.01;
        driveConfig.Slot1.kP = 0.03; 
        driveConfig.Slot1.kI = 0.02;
        driveConfig.Slot1.kD = 0;
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.MotorOutput.DutyCycleNeutralDeadband = 0.1;
        NFRTalonFX driveMotor = new NFRTalonFX(canbus, driveConfig, driveID); 
        driveMotor.getSelectedEncoder().setConversionFactor(Mk3SwerveConstants.kWheelCircumference /
            Mk3SwerveConstants.kDriveGearRatioSlow);
        driveMotor.setInverted(invertDrive);

        TalonFXConfiguration turnConfig = new TalonFXConfiguration();
        turnConfig.CurrentLimits.SupplyCurrentLimit = 40;
        turnConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        turnConfig.CurrentLimits.SupplyTimeThreshold = 0.5;
        turnConfig.Audio.AllowMusicDurDisable = true;
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
        return new SwerveModule(config, driveMotor, turnMotor, Optional.empty());
    }
    public static class Mk4iConstants
    {
        public static final double kDriveGearRatioL2 = 6.75;
        public static final double kDriveGearRatioL3 = 6.12;
        public static final double kTurnGearRatio = 150.0 / 7;
        public static final double kWheelRadius = Units.inchesToMeters(2);
        public static final double kWheelCircumference = 2 * kWheelRadius * Math.PI;
        public static final double kDriveP = 1;
        public static final double kTurnP = 5;
        public static final double kMaxDriveSpeed = Units.feetToMeters(18.2);
    }
    /**
     * Creates a traditional Mk4i swerve module with L3 gearing and two falcon 500s, as well as a cancoder.
     * @param name the name of the module
     * @param driveID the id of the drive motor.
     * @param turnID the id of the turn motor.
     * @param cancoderID the id of the cancoder.
     * @param invertDrive whether to invert the drive motor.
     * @param canbus the name of the canbus
     * @return a new NFRSwerveModule
     */
    public static SwerveModule createMk4iL3(String name, int driveID, int turnID, int cancoderID, boolean invertDrive, String canbus)
    {
        NFRSwerveModuleConfiguration config = new NFRSwerveModuleConfiguration(name)
            .withGearRatios(Mk4iConstants.kDriveGearRatioL3, Mk4iConstants.kTurnGearRatio)
            .withGearboxes(DCMotor.getFalcon500(1), DCMotor.getFalcon500(1))
            .withMOIs(1.2, 1.2)
            .withMaxSpeed(Mk4iConstants.kMaxDriveSpeed);
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 40;
        driveConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        driveConfig.CurrentLimits.SupplyTimeThreshold = 0.01;
        driveConfig.CurrentLimits.StatorCurrentLimit = 40;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.2;
        driveConfig.Audio.AllowMusicDurDisable = true;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.Slot0.kP = 0.01;
        driveConfig.Slot0.kI = 0.1;
        driveConfig.Slot0.kV = 0.01;
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.MotorOutput.DutyCycleNeutralDeadband = 0.1;
        NFRTalonFX driveMotor = new NFRTalonFX(canbus, driveConfig, driveID);
        driveMotor.getSelectedEncoder().setConversionFactor(Mk4iConstants.kWheelCircumference /
            Mk4iConstants.kDriveGearRatioL3);
        driveMotor.setInverted(invertDrive);
        TalonFXConfiguration turnConfig = new TalonFXConfiguration();
        turnConfig.CurrentLimits.SupplyCurrentLimit = 40;
        turnConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        turnConfig.CurrentLimits.StatorCurrentLimit = 40;
        turnConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        turnConfig.CurrentLimits.SupplyTimeThreshold = 0.01;
        turnConfig.Audio.AllowMusicDurDisable = true;
        turnConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        turnConfig.Slot0.kP = Mk4iConstants.kTurnP;
        turnConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.2;
        turnConfig.ClosedLoopGeneral.ContinuousWrap = true;
        NFRTalonFX turnMotor = new NFRTalonFX(canbus, turnConfig, turnID);
        turnMotor.setInverted(true);
        NFRCANCoder cancoder = new NFRCANCoder(canbus, cancoderID);
        try
        {
            turnMotor.setSelectedEncoder(cancoder);
        }
        catch (MotorEncoderMismatchException e)
        {
            e.printStackTrace();
        }
        return new SwerveModule(config, driveMotor, turnMotor, Optional.empty());
    }
    /**
     * Creates a traditional Mk4i swerve module with L2 gearing and two falcon 500s, as well as a cancoder.
     * @param name the name of the module
     * @param driveID the id of the drive motor.
     * @param turnID the id of the turn motor.
     * @param cancoderID the id of the cancoder.
     * @param invertDrive whether to invert the drive motor.
     * @param canbus the name of the canbus
     * @return a new NFRSwerveModule
     */
    public static SwerveModule createMk4iL2(String name, int driveID, int turnID, int cancoderID, boolean invertDrive, String canbus)
    {
        NFRSwerveModuleConfiguration config = new NFRSwerveModuleConfiguration(name)
            .withGearRatios(Mk4iConstants.kDriveGearRatioL3, Mk4iConstants.kTurnGearRatio)
            .withGearboxes(DCMotor.getFalcon500(1), DCMotor.getFalcon500(1))
            .withMOIs(1.2, 1.2)
            .withMaxSpeed(Mk4iConstants.kMaxDriveSpeed);
        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 40;
        driveConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        driveConfig.CurrentLimits.SupplyTimeThreshold = 0.01;
        driveConfig.CurrentLimits.StatorCurrentLimit = 40;
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.2;
        driveConfig.Audio.AllowMusicDurDisable = true;
        driveConfig.Slot0.kP = 0.01;
        driveConfig.Slot0.kI = 0.1;
        driveConfig.Slot0.kV = 0.01;
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.MotorOutput.DutyCycleNeutralDeadband = 0.1;
        NFRTalonFX driveMotor = new NFRTalonFX(canbus, driveConfig, driveID);
        driveMotor.getSelectedEncoder().setConversionFactor(Mk4iConstants.kWheelCircumference /
            Mk4iConstants.kDriveGearRatioL2);
        driveMotor.setInverted(invertDrive);
        TalonFXConfiguration turnConfig = new TalonFXConfiguration();
        turnConfig.CurrentLimits.SupplyCurrentLimit = 40;
        turnConfig.CurrentLimits.SupplyCurrentThreshold = 40;
        turnConfig.CurrentLimits.StatorCurrentLimit = 40;
        turnConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        turnConfig.CurrentLimits.SupplyTimeThreshold = 0.01;
        turnConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.2;
        turnConfig.Audio.AllowMusicDurDisable = true;
        turnConfig.Slot0.kP = 10;
        turnConfig.Slot0.kD = 0.07;
        turnConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0.4;
        turnConfig.ClosedLoopGeneral.ContinuousWrap = true;
        NFRTalonFX turnMotor = new NFRTalonFX(canbus, turnConfig, turnID);
        turnMotor.setInverted(true);
        NFRCANCoder cancoder = new NFRCANCoder(canbus, cancoderID);
        try
        {
            turnMotor.setSelectedEncoder(cancoder);
        }
        catch (MotorEncoderMismatchException e)
        {
            e.printStackTrace();
        }
        return new SwerveModule(config, driveMotor, turnMotor, Optional.empty());
    }
}
