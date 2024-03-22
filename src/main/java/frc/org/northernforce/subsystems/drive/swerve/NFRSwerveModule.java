package org.northernforce.subsystems.drive.swerve;

import java.util.Optional;

import org.northernforce.encoders.NFRAbsoluteEncoder;
import org.northernforce.encoders.NFRCANCoder;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRMotorController;
import org.northernforce.motors.NFRTalonFX;
import org.northernforce.subsystems.NFRSubsystem;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;

/**
 * Class responsible for generic swerve modules.
 */
public class NFRSwerveModule extends NFRSubsystem
{
    /**
     * The configuration for a swerve module.
     */
    public static class NFRSwerveModuleConfiguration extends NFRSubsystemConfiguration
    {
        protected DCMotor driveGearbox, turnGearbox;
        protected double driveGearRatio, turnGearRatio, driveMOI, turnMOI, maxSpeed;
        /**
         * Creates a new basic swerve module configuration. All doubles are initialized to zero.
         * @param name the name of the swerve module (ex. Back Left)
         */
        public NFRSwerveModuleConfiguration(String name)
        {
            super(name);
            driveGearbox = null;
            turnGearbox = null;
            driveGearRatio = 0;
            turnGearRatio = 0;
            driveMOI = 0;
            turnMOI = 0;
            maxSpeed = 0;
        }
        /**
         * Creates a new swerve module configuration.
         * @param name the name of the swerve module (ex. Back Left)
         * @param driveGearbox the gearbox for the drive motor. Only needed for simulation.
         * @param turnGearbox the gearbox for the turn motor. Only needed for simulation.
         * @param driveGearRatio the gear ratio for the drive shaft. Only used in simulation.
         * @param turnGearRatio the gear ratio for the turn shaft. Only used in simulation.
         * @param driveMOI the drive moment of inertia. Only used in simulation.
         * @param turnMOI the turn moment of inertia. Only used in simulation.
         * @param maxSpeed the maximum speed of the drive. Used for closed-loop conversions from [-1, 1] to [-maxSpeed, maxSpeed].
         */
        public NFRSwerveModuleConfiguration(String name, DCMotor driveGearbox, DCMotor turnGearbox,
            double driveGearRatio, double turnGearRatio, double driveMOI, double turnMOI, double maxSpeed)
        {
            super(name);
            this.driveGearbox = driveGearbox;
            this.turnGearbox = turnGearbox;
            this.driveGearRatio = driveGearRatio;
            this.turnGearRatio = turnGearRatio;
            this.driveMOI = driveMOI;
            this.turnMOI = turnMOI;
            this.maxSpeed = maxSpeed;
        }
        /**
         * With gearboxes.
         * @param driveGearbox the gearbox for the drive motor. Only needed for simulation.
         * @param turnGearbox the gearbox for the turn motor. Only needed for simulation.
         * @return this
         */
        public NFRSwerveModuleConfiguration withGearboxes(DCMotor driveGearbox, DCMotor turnGearbox)
        {
            this.driveGearbox = driveGearbox;
            this.turnGearbox = turnGearbox;
            return this;
        }
        /**
         * With gear ratios.
         * @param driveGearRatio the gear ratio for the drive shaft. Only used in simulation.
         * @param turnGearRatio the gear ratio for the turn shaft. Only used in simulation.
         * @return
         */
        public NFRSwerveModuleConfiguration withGearRatios(double driveGearRatio, double turnGearRatio)
        {
            this.driveGearRatio = driveGearRatio;
            this.turnGearRatio = turnGearRatio;
            return this;
        }
        /**
         * With moments of inertias.
         * @param driveMOI the drive moment of inertia. Only used in simulation.
         * @param turnMOI the turn moment of inertia. Only used in simulation.
         * @return this
         */
        public NFRSwerveModuleConfiguration withMOIs(double driveMOI, double turnMOI)
        {
            this.driveMOI = driveMOI;
            this.turnMOI = turnMOI;
            return this;
        }
        /**
         * With maximum speed of drive.
         * @param maxSpeed the maximum speed of the drive. Used for closed-loop conversions from [-1, 1] to [-maxSpeed, maxSpeed].
         * @return this
         */
        public NFRSwerveModuleConfiguration withMaxSpeed(double maxSpeed)
        {
            this.maxSpeed = maxSpeed;
            return this;
        }
    }
    protected final NFRSwerveModuleConfiguration config;
    protected final NFRMotorController driveController, turnController;
    protected final Optional<NFRAbsoluteEncoder> externalEncoder;
    protected final FlywheelSim driveSim, turnSim;
    /**
     * Creates a new NFRSwerveModule.
     * @param config the configuration for the swerve module.
     * @param driveController the drive controller.
     * @param turnController the turn controller.
     * @param externalEncoder the external encoder, if not linked directly to the turnController.
     */
    public NFRSwerveModule(NFRSwerveModuleConfiguration config, NFRMotorController driveController,
        NFRMotorController turnController, Optional<NFRAbsoluteEncoder> externalEncoder)
    {
        super(config);
        this.config = config;
        this.driveController = driveController;
        this.turnController = turnController;
        this.externalEncoder = externalEncoder;
        if (RobotBase.isSimulation())
        {
            driveSim = new FlywheelSim(config.driveGearbox, config.driveGearRatio, config.driveMOI);
            turnSim = new FlywheelSim(config.turnGearbox, config.turnGearRatio, config.turnMOI);
        }
        else
        {
            driveSim = null;
            turnSim = null;
        }
    }
    /**
     * Gets the current rotation of the swerve module.
     * @return Rotation2d representing current swerve module rotation.
     */
    public Rotation2d getRotation()
    {
        return externalEncoder.isPresent() ? Rotation2d.fromRotations(externalEncoder.get().getAbsolutePosition())
            : Rotation2d.fromRotations(turnController.getSelectedEncoder().getPosition());
    }
    /**
     * Gets the velocity of the swerve module.
     * @return velocity in units according to selected encoder.
     */
    public double getVelocity()
    {
        return driveController.getSelectedEncoder().getVelocity();
    }
    /**
     * Gets the state of the swerve module.
     * @return the state (the rotation and speed)
     */
    public SwerveModuleState getState()
    {
        return new SwerveModuleState(getVelocity(), getRotation());
    }
    /**
     * Gets the distance driven by the swerve module.
     * @return the distance driven by the swerve module.
     */
    public double getDistance()
    {
        return driveController.getSelectedEncoder().getPosition();
    }
    /**
     * Gets the position of the swerve module.
     * @return the position (the rotation and distance)
     */
    public SwerveModulePosition getPosition()
    {
        return new SwerveModulePosition(getDistance(), getRotation());
    }
    /**
     * Sets the speed of the drive motor using open-loop control.
     * @param velocity the speed of the drive motor between [-1, 1]
     */
    public void setDriveSpeed(double velocity)
    {
        driveController.set(velocity);
    }
    /**
     * Sets the speed of the drive motor using closed-loop control.
     * @param velocity the speed of the drive motor in units relative to the selected encoder.
     * @param pidSlot the pid slot for closed-loop control.
     */
    public void setDriveSpeed(double velocity, int pidSlot)
    {
        driveController.setVelocity(pidSlot, velocity);
    }
    /**
     * Sets the speed of the turn motor using open-loop control.
     * @param velocity the turn speed of the motor controller between [-1, 1].
     */
    public void setTurnSpeed(double velocity)
    {
        turnController.set(velocity);
    }
    /**
     * Sets the position of the turn motor using closed-loop control.
     * @param position the target rotation of the module.
     * @param pidSlot the pid slot of the closed-loop control.
     * @param useTrapezoidalPositioning whether to use trapezoidal positioning.
     */
    public void setTurnPosition(Rotation2d position, int pidSlot, boolean useTrapezoidalPositioning)
    {
        if (useTrapezoidalPositioning)
        {
            turnController.setPositionTrapezoidal(pidSlot,
                MathUtil.inputModulus(position.getRotations(), 0, 1));
        }
        else
        {
            turnController.setPosition(pidSlot, MathUtil.inputModulus(position.getRotations(), 0, 1));
        }
    }
    /**
     * Updates the simulation model.
     */
    @Override
    public void simulationPeriodic()
    {
        driveSim.setInputVoltage(driveController.getSimulationOutputVoltage());
        turnSim.setInputVoltage(turnController.getSimulationOutputVoltage());
        driveSim.update(0.02);
        turnSim.update(0.02);
        driveController.getSelectedEncoder().addSimulationPosition(driveSim.getAngularVelocityRPM() / 60 * 0.02);
        driveController.getSelectedEncoder().setSimulationVelocity(driveSim.getAngularVelocityRPM() / 60);
        if (externalEncoder.isPresent())
        {
            externalEncoder.get().addAbsoluteSimulationPosition(turnSim.getAngularVelocityRPM() / 60 * 0.02);
            externalEncoder.get().setAbsoluteSimulationVelocity(turnSim.getAngularVelocityRPM() / 60);
        }
        else
        {
            turnController.getSelectedEncoder().addSimulationPosition(turnSim.getAngularVelocityRPM() / 60 * 0.02);
            turnController.getSelectedEncoder().setSimulationVelocity(turnSim.getAngularVelocityRPM() / 60);
        }
    }
    /**
     * Scales the speed of the swerve module state based on the configuration's max speed.
     * @param state the initial state
     * @return the scaled state
     */
    public SwerveModuleState scaleSpeed(SwerveModuleState state)
    {
        return new SwerveModuleState(state.speedMetersPerSecond * config.maxSpeed, state.angle);
    }
    public void resetAngle(Rotation2d angle)
    {
        if (externalEncoder.isPresent())
        {
            externalEncoder.get().setAbsoluteOffset(MathUtil.inputModulus(angle.getRotations() - externalEncoder.get().getAbsolutePosition()
                + externalEncoder.get().getAbsoluteOffset(), 0, externalEncoder.get().getConversionFactor()));
        }
        else if (turnController.getSelectedEncoder() instanceof NFRAbsoluteEncoder)
        {
            NFRAbsoluteEncoder encoder = (NFRAbsoluteEncoder)turnController.getSelectedEncoder();
            encoder.setAbsoluteOffset(MathUtil.inputModulus(angle.getRotations() - encoder.getAbsolutePosition()
                + encoder.getAbsoluteOffset(), 0, encoder.getConversionFactor()));
        }
    }
    /**
     * Initializes the sendable data that is passed when published to network tables.
     * @param builder the SendableBuilder
     */
    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addDoubleProperty("Velocity", () -> getVelocity(), null);
        builder.addDoubleProperty("Angle", () -> getRotation().getDegrees(), null);
        builder.addDoubleProperty("Target Angle", () -> turnController.getTargetPosition(), null);
        builder.addDoubleProperty("Angle Rotations", () -> turnController.getSelectedEncoder().getPosition(), null);
        if (RobotBase.isSimulation())
        {
            builder.addDoubleProperty("Drive - Simulation Voltage", driveController::getSimulationOutputVoltage, null);
            builder.addDoubleProperty("Turn - Simulation Voltage", turnController::getSimulationOutputVoltage, null);
            builder.addDoubleProperty("Drive - Estimated Speed", driveSim::getAngularVelocityRPM, null);
            builder.addDoubleProperty("Turn - Estimated Speed", turnSim::getAngularVelocityRPM, null);
        }
    }
    /**
     * Constants for the Mk3 swerve module.
     */
    public static final class Mk3SwerveConstants
    {
        public static final double kDriveGearRatioSlow = 8.16;
        public static final double kDriveGearRatioFast = 6.86;
        public static final double kTurnGearRatio = 12.8;
        public static final double kWheelRadius = Units.inchesToMeters(2);
        public static final double kWheelCircumference = kWheelRadius * 2 * Math.PI;
        public static final double kDriveMaxSpeed = Units.feetToMeters(13);
        public static final double kDriveP = 1;
        public static final double kTurnP = 2;
    }
    /**
     * Creates a traditional Mk3 swerve module with standard gearing and two falcon 500s, as well as a cancoder.
     * @param name the name of the module
     * @param driveID the id of the drive motor.
     * @param turnID the id of the turn motor.
     * @param cancoderID the id of the cancoder.
     * @param invertDrive whether to invert the drive motor.
     * @return
     */
    public static NFRSwerveModule createMk3Slow(String name, int driveID, int turnID, int cancoderID, boolean invertDrive)
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
        NFRTalonFX driveMotor = new NFRTalonFX(driveConfig, driveID);
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
        NFRTalonFX turnMotor = new NFRTalonFX(turnConfig, turnID);
        NFRCANCoder cancoder = new NFRCANCoder(cancoderID);
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
}
