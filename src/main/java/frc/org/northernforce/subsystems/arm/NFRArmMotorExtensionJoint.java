package org.northernforce.subsystems.arm;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import org.northernforce.encoders.NFREncoder;
import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;


/** Creates an NFRArmMotorExtensionJoint. */
public class NFRArmMotorExtensionJoint extends NFRArmJoint{

    /**
     *  the config for the {@link NFRArmMotorExtensionJoint} class
     */
    public static class NFRArmMotorExtensionJointConfiguration extends NFRArmJointConfiguration {
        /**
         * This represents the static offset from the end of the last component
         * for example if this was a two join rotational arm and this represented the 2nd joint 
         * it would be the transform between the end of the first arm component to this joint
         * 
         * if this was the first component it would be the transform from the center of the robot to the joint
         */
        protected Transform3d originOffset;
        protected double retractedArmLength;
        protected double extendedArmLength;
        protected Optional<Double> extendedLimit = Optional.empty();
        protected Optional<Double> retractedLimit = Optional.empty();
        protected boolean useLimits = false, useIntegratedLimits = false;

        /**
         * Creates the configuration for an arm motor extension
         * @param name the name of the subsystem
         */
        public NFRArmMotorExtensionJointConfiguration(String name) {
            super(name);
        }

        /**
         * Creates the configuration for an arm motor extension
         * @param name the name of the subsystem to create
         * @param originOffset the offset from the end of the last component to this joint 
         * more explanation in the {@link NFRArmJointConfiguration} super class
         * @param retractedArmLength The length of the arm while fully retracted
         * @param extendedArmLength The length of the arm while fully extended
         */
        public NFRArmMotorExtensionJointConfiguration(String name, Transform3d originOffset, 
            double retractedArmLength, double extendedArmLength, double extendedLimit, double retractedLimit)
        {
            super(name);
            this.originOffset = originOffset;
            this.retractedArmLength = retractedArmLength;
            this.extendedArmLength = extendedArmLength;
            this.extendedLimit = Optional.of(extendedLimit);
            this.retractedLimit = Optional.of(retractedLimit);
        }

        /**
         * set the originOffset
         * @param originOffset the offset from the end of the last component to this joint 
         * more explanation in the {@link NFRArmJointConfiguration} super class
         * @return returns this for chaining
         */
        public NFRArmMotorExtensionJointConfiguration withStartPosition(Transform3d originOffset) {
            this.originOffset = originOffset;
            return this;
        }

        /**
         * set the arm length
         * @param extendedArmLength The length of the arm while fully extended
         * @return returns this for chaining
         */
        public NFRArmMotorExtensionJointConfiguration withExtendedArmLength(double extendedArmLength) {
            this.extendedArmLength = extendedArmLength;
            return this;
        }

        /**
         * set the arm length
         * @param retractedArmLength The length of the arm while fully retracted
         * @return returns this for chaining
         */
        public NFRArmMotorExtensionJointConfiguration withRetractedArmLength(double retractedArmLength) {
            this.retractedArmLength = retractedArmLength;
            return this;
        }

        /**
         * set the extended limit
         * @param extendedLimit the extended limit
         * @return returns this for chaining
         */
        public NFRArmMotorExtensionJointConfiguration withExtendedLimit(double extendedLimit) {
            this.extendedLimit = Optional.of(extendedLimit);
            return this;
        }

        /**
         * set the negative limit
         * @param retractedLimit the retracted distance limit
         * @return returns this for chaining
         */
        public NFRArmMotorExtensionJointConfiguration withRetractedLimit(double retractedLimit) {
            this.retractedLimit = Optional.of(retractedLimit);
            return this;
        }
    }

    private final NFRArmMotorExtensionJointConfiguration config;
    private final NFRMotorController motor;
    private final Optional<NFREncoder> externalEncoder;
    private final Optional<BooleanSupplier> retractedLimitSwitch, extendedLimitSwitch;

    /**
     * Creates a new NFRArmMotorExtensionJoint
     * @param config the corresponding config class
     * @param motor the motor to use
     * @param externalEncoder the encoder to use
     */
    public NFRArmMotorExtensionJoint(NFRArmMotorExtensionJointConfiguration config, NFRMotorController motor,
        Optional<NFREncoder> externalEncoder, Optional<BooleanSupplier> retractedLimitSwitch,
        Optional<BooleanSupplier> extendedLimitSwitch)
    {
        super(config);
        this.config = config;
        this.externalEncoder = externalEncoder;
        this.motor = motor;
        this.retractedLimitSwitch = retractedLimitSwitch;
        this.extendedLimitSwitch = extendedLimitSwitch;
        if (config.useLimits && config.useIntegratedLimits && motor.getSelectedEncoder() != null)
        {
            if (config.extendedLimit.isPresent())
            {
                motor.setPositiveLimit(config.extendedLimit.get());
            }
            if (config.retractedLimit.isPresent())
            {
                motor.setNegativeLimit(config.retractedLimit.get());
            }
        }
    }
    /**
     * Returns whether it is extended.
     * @return whether it is extended.
     */
    public boolean isExtended()
    {
        if (extendedLimitSwitch.isPresent())
        {
            return extendedLimitSwitch.get().getAsBoolean();
        }
        else if (externalEncoder.isPresent())
        {
            return externalEncoder.get().getPosition() >= config.extendedArmLength;
        }
        else if (motor.getSelectedEncoder() != null)
        {
            return motor.getSelectedEncoder().getPosition() >= config.extendedArmLength;
        }
        return false;
    }
    /**
     * Returns whether it is retracted.
     * @return whether it is retracted.
     */
    public boolean isRetracted()
    {
        if (retractedLimitSwitch.isPresent())
        {
            return extendedLimitSwitch.get().getAsBoolean();
        }
        else if (externalEncoder.isPresent())
        {
            return externalEncoder.get().getPosition() <= config.retractedArmLength;
        }
        else if (motor.getSelectedEncoder() != null)
        {
            return motor.getSelectedEncoder().getPosition() <= config.retractedArmLength;
        }
        return false;
    }
    /**
     * Sets the open-loop speed.
     * @param speed the speed to run at between [-1, 1]
     */
    public void setOpenLoop(double speed)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !isExtended()))
        {
            motor.set(speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !isRetracted()))
        {
            motor.set(speed);
        }
        else
        {
            motor.set(0);
        }
    }
    /**
     * Sets the closed-loop speed.
     * @param speed the speed to run at relative to the motor
     * @param pidSlot the pid slot of the velocity closed-loop.
     */
    public void setClosedLoop(double speed, int pidSlot)
    {
        if (speed > 0 && (!config.useLimits || config.useIntegratedLimits || !isExtended()))
        {
            motor.setVelocity(pidSlot, speed);
        }
        else if (speed < 0 && (!config.useLimits || config.useIntegratedLimits || !isRetracted()))
        {
            motor.setVelocity(pidSlot, speed);
        }
        else
        {
            motor.setVelocity(pidSlot, 0);
        }
    }
    /**
     * gets the length of the arm length in this case is the distance past the fully retracted point
     * @return returns the length as defined above
     */
    public double getLength() {
        double dLength;
        if (externalEncoder.isPresent()) dLength = externalEncoder.get().getPosition();
        else dLength = motor.getSelectedEncoder().getPosition();
        return dLength;
    }
    @Override
    public Transform3d getEndState() {
        return config.originOffset.plus(new Transform3d(new Translation3d(getLength(), 0, 0), new Rotation3d()));
    }
    /**
     * Gets the motor controller controlling the extension.
     * @return a NFRMotorController.
     */
    public NFRMotorController getController()
    {
        return motor;
    }
}
