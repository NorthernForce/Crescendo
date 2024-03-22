package org.northernforce.motors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.northernforce.encoders.NFRAbsoluteEncoder;
import org.northernforce.encoders.NFREncoder;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * A class that provides a hardware interface for the Spark Max. Inherits from NFRMotorController
 */
public class NFRSparkMax extends CANSparkMax implements NFRMotorController
{
    /**
     * The CANSpark Max's primary (integrated typically) hall sensor.
     */
    public class IntegratedEncoder implements NFREncoder
    {
        /**
         * Checks to see whether an encoder is present by checking if the motor controller is present.
         * @return true if the encoder is present, false if not present
         */
        @Override
        public boolean isPresent()
        {
            return NFRSparkMax.this.isPresent();
        }
        /**
         * Gets the position recorded by the sensor. This will return the relative position for any relative encoder,
         * and may return relative or absolute for an absolute encoder depending on how it is configured and implemented.
         * This value will be inverted as specified, and will be affected by the conversion factor.
         * @return Position affected by conversion factor, rotations by default.
         */
        @Override
        public double getPosition()
        {
            return getEncoder().getPosition();
        }
        /**
         * Sets the inversion of the encoder. This affects getVelocity and getPosition. Remember, however, this is all relative
         * to how the encoder is placed on the shaft.
         * @param inverted whether to invert the encoder readings
         */
        @Override
        public void setInverted(boolean inverted)
        {
            getEncoder().setInverted(inverted);
        }
        /**
         * Gets the velocity recorded by the sensor. This will return the velocity measured by the sensor, either the relative part,
         * or absolute depending on configuration. The value will be inverted as specified, and will be affected by the conversion
         * factor.
         * @return Velocity affected by conversion factor, rotations per second by default.
         */
        @Override
        public double getVelocity() {
            return getEncoder().getVelocity();
        }
        /**
         * Sets the conversion factor that affects readings of the sensor. This is by default 1. 
         * @param factor the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
         */
        @Override
        public void setConversionFactor(double factor)
        {
            getEncoder().setPositionConversionFactor(factor);
            getEncoder().setVelocityConversionFactor(factor / 60);
        }
        /**
         * Resets the position of the encoder. Useful for relative applications.
         * @param position of the encoder which is affected by the conversion factor.
         */
        @Override
        public void setEncoderPosition(double position)
        {
            getEncoder().setPosition(position);
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Deprecated
        @Override
        public void setSimulationPosition(double position)
        {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Deprecated
        @Override
        public void addSimulationPosition(double position)
        {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param velocity
         */
        @Deprecated
        @Override
        public void setSimulationVelocity(double velocity)
        {
        }
        /**
         * Gets the conversion factor that affects readings of the sensor. This is by default 1. 
         * @return the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
         */
        @Override
        public double getConversionFactor()
        {
            return getEncoder().getPositionConversionFactor();
        }
    }
    private final ArrayList<CANSparkMax> followers;
    private final IntegratedEncoder integratedEncoder;
    private NFREncoder selectedEncoder;
    /**
     * Creates a new NFR Spark Max.
     * @param type The Rev Motor Type. (Brushed or Brushless)
     * @param primaryId The primary id of the motor controller
     * @param followerIds the ids of the follower controllers
     */
    public NFRSparkMax(MotorType type, int primaryId, int... followerIds)
    {
        super(primaryId, type);
        followers = new ArrayList<>();
        for (int id : followerIds)
        {
            followers.add(new CANSparkMax(id, type));
        }
        for (var follower : followers)
        {
            follower.follow(this);
        }
        integratedEncoder = new IntegratedEncoder();
        selectedEncoder = integratedEncoder;
    }
    /**
     * Creates a new NFR Spark Max.
     * @param type The Rev Motor Type. (Brushed or Brushless)
     * @param primaryID The primary id of the motor controller
     */
    public NFRSparkMax(MotorType type, int primaryID)
    {
        this(type, primaryID, new int[]{});
    }
    /**
     * Checks to see whether an motor controller is present through various means.
     * @return true if the motor controller is present, false if not present
     */
    @Override
    public boolean isPresent()
    {
        return getFirmwareVersion() == 0;
    }
    /**
     * Gets the number of motors in the NFRMotorController interface.
     * @return number of motor controllers
     */
    @Override
    public int getNumberOfMotors()
    {
        return 1 + followers.size();
    }
    /**
     * Returns a list of the WPILib interface for individual motor controllers.
     * @return a list of all of the motor controllers including the primary and the followers
     */
    @Override
    public List<MotorController> getMotorControllers()
    {
        ArrayList<MotorController> controllers = new ArrayList<>();
        controllers.add(this);
        controllers.addAll(followers);
        return controllers;
    }
    /**
     * Sets the selected encoder to a specific encoder that inherits from NFREncoder. This should be used only with
     * encoders that are compatible with the motor controller.
     * @param encoder to link to the motor controller
     * @throws MotorEncoderMismatchException an exception if the encoder is not compatible to be directly linked to 
     * the motor
     */
    @Override
    public void setSelectedEncoder(NFREncoder encoder) throws MotorEncoderMismatchException
    {
        if (encoder instanceof IntegratedEncoder)
        {
            selectedEncoder = encoder;
            getPIDController().setFeedbackDevice(getEncoder());
        }
        else if (encoder instanceof AbsoluteEncoder)
        {
            selectedEncoder = encoder;
            getPIDController().setFeedbackDevice(getAbsoluteEncoder(Type.kDutyCycle));
        }
    }
    /**
     * Returns the selected encoder that is linked to the motor controller. By default, the integrated encoder is chosen.
     * @return the selected encoder
     */
    @Override
    public NFREncoder getSelectedEncoder()
    {
        return selectedEncoder;
    }
    /**
     * Returns the integrated encoder built into the motor it is controller. This is different from an alternate quadrature
     * that is separately pluggen into the motor controller.
     * @return the integrated encoder
     */
    @Override
    public NFREncoder getIntegratedEncoder()
    {
        return integratedEncoder;
    }
    /**
     * Returns the external quadrature encoder plugged into the motor. May not be able to tell if the external quadrature
     * is present however.
     * @return optionally, the external quadrature encdoer
     */
    @Override
    public Optional<NFREncoder> getExternalQuadratureEncoder()
    {
        return Optional.empty();
    }
    /**
     * A Rev absolute encoder plugged into the absolute breakout board.
     */
    public class AbsoluteEncoder implements NFRAbsoluteEncoder
    {
        /**
         * Checks to see whether an encoder is present by checking if the motor controller is present.
         * @return true if the encoder is present, false if not present
         */
        @Override
        public boolean isPresent()
        {
            return NFRSparkMax.this.isPresent();
        }
        /**
         * Gets the position recorded by the sensor. This will return the position for an absolute encoder.
         * This value will be inverted as specified, and will be affected by the conversion factor.
         * @return Position affected by conversion factor, rotations by default.
         */
        @Override
        public double getPosition()
        {
            return getAbsoluteEncoder(Type.kDutyCycle).getPosition();
        }
        /**
         * Sets the inversion of the encoder. This affects getVelocity and getPosition. Remember, however, this is all relative
         * to how the encoder is placed on the shaft.
         * @param inverted whether to invert the encoder readings
         */
        @Override
        public void setInverted(boolean inverted)
        {
            getAbsoluteEncoder(Type.kDutyCycle).setInverted(inverted);
        }
        /**
         * Gets the velocity recorded by the sensor. This will return the velocity measured by the sensor, either the relative part,
         * or absolute depending on configuration. The value will be inverted as specified, and will be affected by the conversion
         * factor.
         * @return Velocity affected by conversion factor, rotations per second by default.
         */
        @Override
        public double getVelocity()
        {
            return getAbsoluteEncoder(Type.kDutyCycle).getVelocity();
        }
        /**
         * Sets the conversion factor that affects readings of the sensor. This is by default 1. 
         * @param factor the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
         */
        @Override
        public void setConversionFactor(double factor)
        {
            getAbsoluteEncoder(Type.kDutyCycle).setPositionConversionFactor(factor);
            getAbsoluteEncoder(Type.kDutyCycle).setVelocityConversionFactor(factor);
        }
        /**
         * Resets the position of the encoder. Useful for relative applications.
         * @param position of the encoder which is affected by the conversion factor.
         */
        @Override
        public void setEncoderPosition(double position)
        {
            getAbsoluteEncoder(Type.kDutyCycle).setZeroOffset(
                position - getAbsoluteEncoder(Type.kDutyCycle).getPosition() + getAbsoluteEncoder(Type.kDutyCycle).getZeroOffset()
            );
        } 
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Override
        @Deprecated
        public void setSimulationPosition(double position)
        {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Override
        @Deprecated
        public void addSimulationPosition(double position)
        {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param velocity
         */
        @Override
        @Deprecated
        public void setSimulationVelocity(double velocity) {
        }
        /**
         * Gets the position recorded by the sensor. This will return the relative position for the absolute sensor.
         * This value will be inverted as specified, and will be affected by the conversion factor.
         * @return Position affected by conversion factor, rotations by default.
         */
        @Override
        public double getAbsolutePosition()
        {
            return getAbsoluteEncoder(Type.kDutyCycle).getPosition();
        }
        /**
         * Gets the velocity recorded by the sensor. This will return the velocity measured by the absolute sensor.
         * The value will be inverted as specified, and will be affected by the conversion factor.
         * @return Velocity affected by conversion factor, rotations per second by default.
         */
        @Override
        public double getAbsoluteVelocity() {
            return getAbsoluteEncoder(Type.kDutyCycle).getVelocity();
        }
        /**
         * Sets the inversion of the encoder. This affects getVelocity and getPosition. Remember, however, this is all relative
         * to how the encoder is placed on the shaft.
         * @param inverted whether to invert the encoder readings
         */
        @Override
        public void setAbsoluteInverted(boolean inverted) {
            getAbsoluteEncoder(Type.kDutyCycle).setInverted(inverted);
        }
        /**
         * Sets the offset of the absolute sensor. This is a permanant configuration, and thus will be saved between boots.
         * @param offset in units based on absolute conversion factor.
         */
        @Override
        public void setAbsoluteOffset(double offset) {
            getAbsoluteEncoder(Type.kDutyCycle).setZeroOffset(offset);
        }
        /**
         * This is a permanant configuration, and thus will be saved between boots.
         * @return offset in units based on absolute conversion factor.
         */
        @Override
        public double getAbsoluteOffset()
        {
            return getAbsoluteEncoder(Type.kDutyCycle).getZeroOffset();
        }
        /**
         * Sets the conversion factor that affects readings of the sensor. This is by default 1. 
         * @param factor the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
         */
        @Override
        public void setAbsoluteConversionFactor(double factor) {
            setConversionFactor(factor);
        }
        /**
         * Sets whether to use absolute measurements in functions inherited from NFREncoder such as getPosition and getVelocity.
         * @param useAbsoluteMeasurements whether to use absolute measurements
         */
        @Override
        public void setUseAbsoluteMeasurements(boolean useAbsoluteMeasurements) {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Override
        @Deprecated
        public void setAbsoluteSimulationPosition(double position) {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param position
         */
        @Override
        @Deprecated
        public void addAbsoluteSimulationPosition(double position) {
        }
        /**
         * Do not trust this. Simulation is not supported by rev.
         * @param velocity
         */
        @Override
        @Deprecated
        public void setAbsoluteSimulationVelocity(double velocity) {
        }
        /**
         * Gets the conversion factor that affects readings of the sensor. This is by default 1. 
         * @return the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
         */
        @Override
        public double getConversionFactor()
        {
            return getAbsoluteEncoder(Type.kDutyCycle).getPositionConversionFactor();
        }
    }
    /**
     * Returns the external absolute encoder plugged into the motor. May not be able to tell if the absolute
     * is present however.
     * @return optionally, the external absolute encoder
     */
    @Override
    public Optional<NFRAbsoluteEncoder> getAbsoluteEncoder()
    {
        return Optional.of(new AbsoluteEncoder());
    }
    /**
     * Sets the velocity of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param velocity the target velocity in terms of the selected sensor's units.
     */
    @Override
    public void setVelocity(int pidSlot, double velocity) {
        getPIDController().setReference(velocity, ControlType.kVelocity, pidSlot);
    }
    /**
     * Sets the velocity of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param velocity the target velocity in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    @Override
    public void setVelocity(int pidSlot, double velocity, double arbitraryFeedforward) {
        getPIDController().setReference(velocity, ControlType.kVelocity, pidSlot, arbitraryFeedforward);
    }
    /**
     * Sets the position of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     */
    @Override
    public void setPosition(int pidSlot, double position) {
        getPIDController().setReference(position, ControlType.kPosition, pidSlot);
    }
    /**
     * Sets the position of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    @Override
    public void setPosition(int pidSlot, double position, double arbitraryFeedforward) {
        getPIDController().setReference(position, ControlType.kPosition, pidSlot);
    }
    /**
     * Sets the position of the motor using trapezoidal closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     */
    @Override
    public void setPositionTrapezoidal(int pidSlot, double position) {
        getPIDController().setReference(position, ControlType.kSmartMotion, pidSlot);
    }
    /**
     * Sets the position of the motor using trapezoidal closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    @Override
    public void setPositionTrapezoidal(int pidSlot, double position, double arbitraryFeedforward) {
        getPIDController().setReference(position, ControlType.kSmartMotion, pidSlot, arbitraryFeedforward);
    }
    /**
     * Gets the simulation output voltage that is calculated by inputs to the motor. This should not be used
     * when not in simulation, as it can lead to undefined behavior.
     * @return the simulation output voltage [-12v..12v]
     */
    @Override
    public double getSimulationOutputVoltage() {
        return get() * RobotController.getBatteryVoltage();
    }
    /**
     * Sets the opposition of a follower so it opposes the inversion of the primary motor.
     * @param idx index of the motor in the opposition
     */
    @Override
    public void setFollowerOppose(int idx) {
        followers.get(idx).follow(this, true);
    }
    /**
     * Sets up a limit that prevent the motor from moving in all modes when past this limit.
     * @param positiveLimit in selected sensor units.
     */
    @Override
    public void setPositiveLimit(double positiveLimit)
    {
        setSoftLimit(SoftLimitDirection.kReverse, (float)positiveLimit);
        enableSoftLimit(SoftLimitDirection.kReverse, true);
    }
    /**
     * Disables the positive limit.
     */
    @Override
    public void disablePositiveLimit()
    {
        enableSoftLimit(SoftLimitDirection.kReverse, false);
    }
    /**
     * Sets up a limit that prevent the motor from moving in all modes when past this limit.
     * @param negativeLimit in selected sensor units.
     */
    @Override
    public void setNegativeLimit(double negativeLimit)
    {
        setSoftLimit(SoftLimitDirection.kForward, (float)negativeLimit);
        enableSoftLimit(SoftLimitDirection.kForward, true);
    }
    /**
     * Disables the negative limit.
     */
    @Override
    public void disableNegativeLimit()
    {
        enableSoftLimit(SoftLimitDirection.kForward, false);
    }
    /**
     * Returns the target position that the closed-loop control is attemped to reach.
     * @return target position in units native to the encoder.
     */
    @Override
    public double getTargetPosition()
    {
        return 0;
    }
}
