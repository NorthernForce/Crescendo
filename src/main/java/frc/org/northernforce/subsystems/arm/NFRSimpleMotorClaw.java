package org.northernforce.subsystems.arm;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import org.northernforce.encoders.NFREncoder;
import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;

/**
 * This is a simple motor claw joint that is intented for a claw that is controlled by a motor, in which one direction opens,
 * and the other closes.
 */
public class NFRSimpleMotorClaw extends NFRArmJoint
{
    /**
     * This is the configuration for a simple motor claw joint.
     */
    public static class NFRSimpleMotorClawConfiguration extends NFRArmJointConfiguration
    {
        protected Transform3d originOffset, offsetToEndOfClaw;
        protected Optional<Rotation2d> openRotation, closedRotation;
        protected boolean useLimits, useIntegratedLimits;
        /**
         * Simple constructor that initializes values to default values (ie. 0 for double).
         * @param name the unique name of the motor claw.
         */
        public NFRSimpleMotorClawConfiguration(String name)
        {
            super(name);
            originOffset = new Transform3d();
            offsetToEndOfClaw = new Transform3d();
            openRotation = Optional.empty();
            closedRotation = Optional.empty();
            useLimits = false;
            useIntegratedLimits = false;
        }
        /**
         * Constructs the configuration. Takes in all of the possible parameters.
         * @param name the unique name of the motor claw.
         * @param originOffset the offset from the end of the previous joint to the start of this joint.
         * @param offsetToEndOfClaw the offset from the start of the claw to the end of the claw/focal point.
         * @param openRotation the open encoder rotation if the claw is open. 
         * @param closedRotation the closed encoder rotation if the claw is closed.
         * @param useLimits whether to use limits
         * @param useIntegratedLimits whether to use the motor controller's ability to use integrated limits.
         */
        public NFRSimpleMotorClawConfiguration(String name, Transform3d originOffset,
            Transform3d offsetToEndOfClaw, Rotation2d openRotation, Rotation2d closedRotation, boolean useLimits,
            boolean useIntegratedLimits)
        {
            super(name);
            this.originOffset = originOffset;
            this.offsetToEndOfClaw = offsetToEndOfClaw;
            this.openRotation = Optional.empty();
            this.closedRotation = Optional.empty();
            this.useLimits = useLimits;
            this.useIntegratedLimits = useIntegratedLimits;
        }
        /**
         * With origin offset
         * @param originOffset the offset from the end of the previous joint to the start of this joint.
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withOriginOffset(Transform3d originOffset)
        {
            this.originOffset = originOffset;
            return this;
        }
        /**
         * With offset to end of claw
         * @param offsetToEndOfClaw the offset from the start of the claw to the end of the claw/focal point.
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withOffsetToEndOfClaw(Transform3d offsetToEndOfClaw)
        {
            this.offsetToEndOfClaw = offsetToEndOfClaw;
            return this;
        }
        /**
         * With open rotation
         * @param openRotation the open encoder rotation if the claw is open. 
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withOpenRotation(Rotation2d openRotation)
        {
            this.openRotation = Optional.of(openRotation);
            return this;
        }
        /**
         * With closed rotation
         * @param closedRotation the closed encoder rotation if the claw is closed.
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withClosedRotation(Rotation2d closedRotation)
        {
            this.closedRotation = Optional.of(closedRotation);
            return this;
        }
        /**
         * With use limits
         * @param useLimits whether to use limits
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withUseLimits(boolean useLimits)
        {
            this.useLimits = useLimits;
            return this;
        }
        /**
         * With use integrated limits
         * @param useIntegratedLimits whether to use the motor controller's ability to use integrated limits.
         * @return this
         */
        public NFRSimpleMotorClawConfiguration withUseIntegratedLimits(boolean useIntegratedLimits)
        {
            this.useIntegratedLimits = useIntegratedLimits;
            return this;
        }
    }
    protected final NFRSimpleMotorClawConfiguration config;
    protected final NFRMotorController controller;
    protected final Optional<BooleanSupplier> closedLimitSwitch, openLimitSwitch;
    protected final Optional<NFREncoder> externalEncoder;
    /**
     * Creates a new NFR Simple Motor Claw
     * @param config the configuration of the claw
     * @param controller the motor controller to control the claw
     * @param externalEncoder the optional external encoder if encoder cannot be directly linked to motor
     * @param closedLimitSwitch the closed limit switch, optional
     * @param openLimitSwitch the open limit switch, optional
     */
    public NFRSimpleMotorClaw(NFRSimpleMotorClawConfiguration config, NFRMotorController controller,
        Optional<NFREncoder> externalEncoder, Optional<BooleanSupplier> closedLimitSwitch,
        Optional<BooleanSupplier> openLimitSwitch)
    {
        super(config);
        this.config = config;
        this.controller = controller;
        this.externalEncoder = externalEncoder;
        this.closedLimitSwitch = closedLimitSwitch;
        this.openLimitSwitch = openLimitSwitch;
        if (externalEncoder.isEmpty() && controller.getSelectedEncoder() != null && config.useLimits && config.useIntegratedLimits)
        {
            if (config.openRotation.isPresent())
            {
                controller.setPositiveLimit(config.openRotation.get().getRotations());
            }
            if (config.closedRotation.isPresent())
            {
                controller.setNegativeLimit(config.closedRotation.get().getRotations());
            }
        }
    }
    /**
     * Checks whether the claw is closed
     * @return whether the claw is closed
     */
    public boolean isClosed()
    {
        if (closedLimitSwitch.isPresent())
        {
            return closedLimitSwitch.get().getAsBoolean();
        }
        else if (config.closedRotation.isPresent())
        {
            if (externalEncoder.isEmpty() && controller.getSelectedEncoder() != null)
            {
                return controller.getSelectedEncoder().getPosition() <= config.closedRotation.get().getRotations();
            }
            else if (externalEncoder.isPresent())
            {
                return externalEncoder.get().getPosition() <= config.closedRotation.get().getRotations();
            }
        }
        return false;
    }
    /**
     * Checks whether the claw is open
     * @return whether the claw is open
     */
    public boolean isOpen()
    {
        if (openLimitSwitch.isPresent())
        {
            return openLimitSwitch.get().getAsBoolean();
        }
        else if (config.openRotation.isPresent())
        {
            if (externalEncoder.isEmpty() && controller.getSelectedEncoder() != null)
            {
                return controller.getSelectedEncoder().getPosition() >= config.openRotation.get().getRotations();
            }
            else if (externalEncoder.isPresent())
            {
                return externalEncoder.get().getPosition() >= config.openRotation.get().getRotations();
            }
        }
        return false;
    }
    /**
     * Gets the end state of the claw (static)
     * @return the end state of the claw
     */
    @Override
    public Transform3d getEndState()
    {
        return config.originOffset.plus(config.offsetToEndOfClaw);
    }
    /**
     * Sets the speed of the motor using open-loop control.
     * @param speed between [-1, 1]
     */
    public void setOpenLoop(double speed)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !isOpen()))
        {
            controller.set(speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !isClosed()))
        {
            controller.set(speed);
        }
        else
        {
            controller.set(0);
        }
    }
    /**
     * Sets the speed of the motor using closed-loop control.
     * @param speed relative to the motor
     * @param pidSlot the pid slot for velocity closed-loop control.
     */
    public void setClosedLoop(double speed, int pidSlot)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !isOpen()))
        {
            controller.setVelocity(pidSlot, speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !isClosed()))
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