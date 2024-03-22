package org.northernforce.subsystems.arm;

import java.util.Optional;

import org.northernforce.encoders.NFREncoder;
import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;

/**
 * A common subsystem for rotating arm joints.
 */
public class NFRRotatingArmJoint extends NFRArmJoint
{
    /**
     * The configuration for the arm. Can be built with builder functions.
     */
    public static class NFRRotatingArmJointConfiguration extends NFRArmJointConfiguration
    {
        protected Transform3d originOffset = new Transform3d();
        protected Optional<Rotation2d> positiveLimit = Optional.empty(), negativeLimit = Optional.empty();
        protected Rotation2d encoderOffset = new Rotation2d();
        protected boolean simulateGravity = false, useLimits = false, useIntegratedLimits = false;
        protected DCMotor gearbox = null;
        protected double gearRatio = 1, length = 0, mass = 0;
        /**
         * Basic constructor that initializes values to defaults. Best to use with the build functionality.
         * @param name the unique name of the rotating arm joint.
         */
        public NFRRotatingArmJointConfiguration(String name)
        {
            super(name);
        }
        /**
         * All inclusive constructor that initializes all of the values.
         * @param name the unique name of the rotating arm joint.
         * @param originOffset the offset from the end of the previous joint, or from the center of the chassis.
         * @param positiveLimit the positive limit of the arm joint.
         * @param negativeLimit the negative limit of the arm joint.
         * @param gearbox the gearbox of the motor. Only necessary if using simulation.
         * @param gearRatio the gear ratio of the motor. Only necessary if using simulation.
         * @param length the length of the arm in meters.
         * @param mass the mass of the arm in kilograms.
         * @param encoderOffset the offset from the encoder value to the arm position such that it is added to the encoder value
         * when getRotation() is called.
         * @param simulateGravity whether or not to simulate gravity.
         */
        public NFRRotatingArmJointConfiguration(String name, Transform3d originOffset, Rotation2d positiveLimit,
            Rotation2d negativeLimit, DCMotor gearbox, double gearRatio, double length, double mass,
            Rotation2d encoderOffset, boolean simulateGravity, boolean useLimits, boolean useIntegratedLimits)
        {
            super(name);
            this.originOffset = originOffset;
            this.positiveLimit = Optional.of(positiveLimit);
            this.negativeLimit = Optional.of(negativeLimit);
            this.gearbox = gearbox;
            this.gearRatio = gearRatio;
            this.length = length;
            this.mass = mass;
            this.simulateGravity = simulateGravity;
            this.useLimits = useLimits;
            this.useIntegratedLimits = useIntegratedLimits;
        }
        /**
         * With origin offset.
         * @param originOffset the offset from the end of the previous joint, or from the center of the chassis.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withOriginOffset(Transform3d originOffset)
        {
            this.originOffset = originOffset;
            return this;
        }
        /**
         * With limits.
         * @param positiveLimit the positive limit of the arm joint.
         * @param negativeLimit the negative limit of the arm joint.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withLimits(Rotation2d positiveLimit, Rotation2d negativeLimit)
        {
            this.positiveLimit = Optional.of(positiveLimit);
            this.negativeLimit = Optional.of(negativeLimit);
            return this;
        }
        /**
         * With gearbox.
         * @param gearbox the gearbox of the motor. Only necessary if using simulation.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withGearbox(DCMotor gearbox)
        {
            this.gearbox = gearbox;
            return this;
        }
        /**
         * With gear ratio.
         * @param gearRatio the gear ratio of the motor. Only necessary if using simulation.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withGearRatio(double gearRatio)
        {
            this.gearRatio = gearRatio;
            return this;
        }
        /**
         * With length.
         * @param length the length of the arm in meters.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withLength(double length)
        {
            this.length = length;
            return this;
        }
        /**
         * With mass.
         * @param mass the mass of the arm in kilograms.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withMass(double mass)
        {
            this.mass = mass;
            return this;
        }
        /**
         * With encoder offset.
         * @param encoderOffset the offset from the encoder value to the arm position such that it is added to the encoder value
         * when getRotation() is called.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withEncoderOffset(Rotation2d encoderOffset)
        {
            this.encoderOffset = encoderOffset;
            return this;
        }
        /**
         * With simulate gravity.
         * @param simulateGravity whether or not to simulate gravity.
         * @return configuration instance
         */
        public NFRRotatingArmJointConfiguration withSimulateGravity(Boolean simulateGravity)
        {
            this.simulateGravity = simulateGravity;
            return this;
        }
        /**
         * With use limits
         * @param useLimits whether to use limits
         * @return this
         */
        public NFRRotatingArmJointConfiguration withUseLimits(boolean useLimits)
        {
            this.useLimits = useLimits;
            return this;
        }
        /**
         * With use integrated limits
         * @param useIntegratedLimits whether to use the motor controller's ability to use integrated limits.
         * @return this
         */
        public NFRRotatingArmJointConfiguration withUseIntegratedLimits(boolean useIntegratedLimits)
        {
            this.useIntegratedLimits = useIntegratedLimits;
            return this;
        }
    }
    protected final NFRRotatingArmJointConfiguration config;
    protected final NFRMotorController controller;
    protected final Optional<NFREncoder> externalEncoder;
    protected final SingleJointedArmSim armSim;
    /**
     * Creates a new NFRRotatingArmJoint
     * @param config the configuration of the nfr rotating arm joint
     * @param controller the motor controller for the rotating arm joint
     * @param externalEncoder the optional external encoder
     */
    public NFRRotatingArmJoint(NFRRotatingArmJointConfiguration config, NFRMotorController controller,
        Optional<NFREncoder> externalEncoder)
    {
        super(config);
        this.config = config;
        this.controller = controller;
        this.externalEncoder = externalEncoder;
        if (externalEncoder.isEmpty() && config.useLimits && config.useIntegratedLimits &&
            controller.getSelectedEncoder() != null)
        {
            if (config.positiveLimit.isPresent())
            {
                controller.setPositiveLimit(config.positiveLimit.get().minus(config.encoderOffset).getRotations());
            }
            if (config.negativeLimit.isPresent())
            {
                controller.setNegativeLimit(config.negativeLimit.get().minus(config.encoderOffset).getRotations());
            }
        }
        if (RobotBase.isSimulation())
        {
            armSim = new SingleJointedArmSim(
                config.gearbox,
                config.gearRatio,
                SingleJointedArmSim.estimateMOI(config.length, config.mass),
                config.length,
                config.negativeLimit.get().getRadians(),
                config.positiveLimit.get().getRadians(),
                config.simulateGravity,
                0
            );
        }
        else
        {
            armSim = null;
        }
    }
    /**
     * Gets the rotation of the arm
     * @return the rotation of the arm
     */
    public Rotation2d getRotation()
    {
        if (externalEncoder.isPresent())
            return Rotation2d.fromRotations(externalEncoder.get().getPosition())
                .plus(config.encoderOffset);
        else
            return Rotation2d.fromRotations(controller.getSelectedEncoder().getPosition())
                .plus(config.encoderOffset);
    }
    /**
     * Updates the simulation data.
     */
    @Override
    public void simulationPeriodic()
    {
        armSim.setInputVoltage(controller.getSimulationOutputVoltage());
        armSim.update(0.02);
        if (externalEncoder.isPresent())
        {
            externalEncoder.get().setSimulationPosition(
                Rotation2d.fromRadians(armSim.getAngleRads()).minus(config.encoderOffset).getRotations()
            );
            externalEncoder.get().setSimulationVelocity(
                Rotation2d.fromRadians(armSim.getVelocityRadPerSec()).minus(config.encoderOffset).getRotations()
            );
        }
        else
        {
            controller.getSelectedEncoder().setSimulationPosition(
                Rotation2d.fromRadians(armSim.getAngleRads()).minus(config.encoderOffset).getRotations()
            );
            controller.getSelectedEncoder().setSimulationVelocity(
                Rotation2d.fromRadians(armSim.getVelocityRadPerSec()).minus(config.encoderOffset).getRotations()
            );
        }
    }
    /**
     * Returns the end state between the last joint and the end of this joint.
     * @return the end state
     */
    @Override
    public Transform3d getEndState()
    {
        return config.originOffset.plus(new Transform3d(
            new Translation3d(),
            new Rotation3d(
                0,
                getRotation().getRadians(),
                0
            )
        ));
    }
    /**
     * Checks to see if at the positive limit.
     * @return whether the arm is equal to or past the positive limit.
     */
    public boolean atPositiveLimit()
    {
        if (config.positiveLimit.isPresent())
        {
            if (externalEncoder.isEmpty() && controller.getSelectedEncoder() != null)
            {
                return controller.getSelectedEncoder().getPosition() >= config.positiveLimit.get().getRotations();
            }
            else if (externalEncoder.isPresent())
            {
                return externalEncoder.get().getPosition() >= config.positiveLimit.get().getRotations();
            }
        }
        return false;
    }
    /**
     * Checks to see if at the negative limit.
     * @return whether the arm is equal to or past the negative limit.
     */
    public boolean atNegativeLimit()
    {
        if (config.negativeLimit.isPresent())
        {
            if (externalEncoder.isEmpty() && controller.getSelectedEncoder() != null)
            {
                return controller.getSelectedEncoder().getPosition() <= config.negativeLimit.get().getRotations();
            }
            else if (externalEncoder.isPresent())
            {
                return externalEncoder.get().getPosition() <= config.negativeLimit.get().getRotations();
            }
        }
        return false;
    }
    /**
     * Sets the open loop speed of the motor.
     * @param speed the speed between [-1, 1]
     */
    public void setOpenLoop(double speed)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !atPositiveLimit()))
        {
            controller.set(speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !atNegativeLimit()))
        {
            controller.set(speed);
        }
        else
        {
            controller.set(0);
        }
    }
    /**
     * Sets the closed-loop speed of the motor.
     * @param speed the speed relative to the motor.
     * @param pidSlot the pid slot of the velocity closed-loop control.
     */
    public void setClosedLoop(double speed, int pidSlot)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !atPositiveLimit()))
        {
            controller.setVelocity(pidSlot, speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !atNegativeLimit()))
        {
            controller.setVelocity(pidSlot, speed);
        }
        else
        {
            controller.setVelocity(pidSlot, 0);
        }
    }
    /**
     * Gets the controller that the joint uses.
     * @return the controller that the joint uses.
     */
    public NFRMotorController getController()
    {
        return controller;
    }
}
