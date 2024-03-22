package org.northernforce.motors;

import java.util.List;
import java.util.Optional;

import org.northernforce.encoders.NFRAbsoluteEncoder;
import org.northernforce.encoders.NFREncoder;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * A NFRMotorController is an interface that defines common funcionality amongst many motor controller types,
 * and it provides utility for dealing with multiple motors following a primary motor.
 */
public interface NFRMotorController extends MotorController
{
    /**
     * Checks to see whether an motor controller is present through various means.
     * @return true if the motor controller is present, false if not present
     */
    public boolean isPresent();
    /**
     * Gets the number of motors in the NFRMotorController interface.
     * @return number of motor controllers
     */
    public int getNumberOfMotors();
    /**
     * Returns a list of the WPILib interface for individual motor controllers.
     * @return a list of all of the motor controllers including the primary and the followers
     */
    public List<MotorController> getMotorControllers();
    /**
     * Sets the inversion of the motor. Affects the integrated sensor, the external quadratures and pulse width absolutes
     * that are directly plugged into the motor.
     * @param inverted whether to invert the motor controller
     */
    public void setInverted(boolean inverted);
    /**
     * Sets the selected encoder to a specific encoder that inherits from NFREncoder. This should be used only with
     * encoders that are compatible with the motor controller.
     * @param encoder to link to the motor controller
     * @throws MotorEncoderMismatchException an exception if the encoder is not compatible to be directly linked to 
     * the motor
     */
    public void setSelectedEncoder(NFREncoder encoder) throws MotorEncoderMismatchException;
    /**
     * Returns the selected encoder that is linked to the motor controller. By default, the integrated encoder is chosen.
     * @return the selected encoder
     */
    public NFREncoder getSelectedEncoder();
    /**
     * Returns the integrated encoder built into the motor it is controller. This is different from an alternate quadrature
     * that is separately pluggen into the motor controller.
     * @return the integrated encoder
     */
    public NFREncoder getIntegratedEncoder();
    /**
     * Returns the external quadrature encoder plugged into the motor. May not be able to tell if the external quadrature
     * is present however.
     * @return optionally, the external quadrature encdoer
     */
    public Optional<NFREncoder> getExternalQuadratureEncoder();
    /**
     * Returns the external absolute encoder plugged into the motor. May not be able to tell if the absolute
     * is present however.
     * @return optionally, the external absolute encoder
     */
    public Optional<NFRAbsoluteEncoder> getAbsoluteEncoder();
    /**
     * Sets the velocity of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param velocity the target velocity in terms of the selected sensor's units.
     */
    public void setVelocity(int pidSlot, double velocity);
    /**
     * Sets the velocity of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param velocity the target velocity in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    public void setVelocity(int pidSlot, double velocity, double arbitraryFeedforward);
    /**
     * Sets the position of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     */
    public void setPosition(int pidSlot, double position);
    /**
     * Sets the position of the motor using closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    public void setPosition(int pidSlot, double position, double arbitraryFeedforward);
    /**
     * Sets the position of the motor using trapezoidal closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     */
    public void setPositionTrapezoidal(int pidSlot, double position);
    /**
     * Sets the position of the motor using trapezoidal closed-loop feedback. Must use a configured pid slot.
     * @param pidSlot the pid slot of the closed-loop configuration.
     * @param position the target position in terms of the selected sensor's units.
     * @param arbitraryFeedforward the arbitrary percentage value applied. Often to counteract gravity.
     * Added to the output calculated by the closed-loop control.
     */
    public void setPositionTrapezoidal(int pidSlot, double position, double arbitraryFeedforward);
    /**
     * Sets the voltage of the motor.
     * @param voltage of the motor. Between -12 volts and 12 volts.
     */
    public void setVoltage(double voltage);
    /**
     * Gets the simulation output voltage that is calculated by inputs to the motor. This should not be used
     * when not in simulation, as it can lead to undefined behavior.
     * @return the simulation output voltage [-12v..12v]
     */
    public double getSimulationOutputVoltage();
    /**
     * Sets the opposition of a follower so it opposes the inversion of the primary motor.
     * @param idx index of the motor in the opposition
     */
    public void setFollowerOppose(int idx);
    /**
     * Sets up a limit that prevent the motor from moving in all modes when past this limit.
     * @param positiveLimit in selected sensor units.
     */
    public void setPositiveLimit(double positiveLimit);
    /**
     * Disables the positive limit.
     */
    public void disablePositiveLimit();
    /**
     * Sets up a limit that prevent the motor from moving in all modes when past this limit.
     * @param negativeLimit in selected sensor units.
     */
    public void setNegativeLimit(double negativeLimit);
    /**
     * Disables the negative limit.
     */
    public void disableNegativeLimit();
    /**
     * Returns the target position that the closed-loop control is attemped to reach.
     * @return target position in units native to the encoder.
     */
    public double getTargetPosition();
}