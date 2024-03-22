package org.northernforce.encoders;

/**
 * A NFR Encoder is common interface that is meant to provide consistent funcionality between encoders.
 * This allows for more modular code and such. However, this said, the NFREncoder is not a type of encoder itself,
 * and you must a create the encoder as another encoder that inherits from NFREncoder.
 */
public interface NFREncoder
{
    /**
     * Checks to see whether an encoder is present through various means. This may not be possible with all types of encoders
     * such as duty cycle encoders plugged into the rio, thus this may return true regardless. Please be aware of this.
     * @return true if the encoder is present, false if not present
     */
    public boolean isPresent();
    /**
     * Gets the position recorded by the sensor. This will return the relative position for any relative encoder,
     * and may return relative or absolute for an absolute encoder depending on how it is configured and implemented.
     * This value will be inverted as specified, and will be affected by the conversion factor.
     * @return Position affected by conversion factor, rotations by default.
     */
    public double getPosition();
    /**
     * Sets the inversion of the encoder. This affects getVelocity and getPosition. Remember, however, this is all relative
     * to how the encoder is placed on the shaft.
     * @param inverted whether to invert the encoder readings
     */
    public void setInverted(boolean inverted);
    /**
     * Gets the velocity recorded by the sensor. This will return the velocity measured by the sensor, either the relative part,
     * or absolute depending on configuration. The value will be inverted as specified, and will be affected by the conversion
     * factor.
     * @return Velocity affected by conversion factor, rotations per second by default.
     */
    public double getVelocity();
    /**
     * Sets the conversion factor that affects readings of the sensor. This is by default 1. 
     * @param factor the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
     */
    public void setConversionFactor(double factor);
    /**
     * Gets the conversion factor that affects readings of the sensor. This is by default 1. 
     * @return the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
     */
    public double getConversionFactor();
    /**
     * Returns whether the encoder has absolute position measurement capabilities. Does not share whether it inherits from
     * NFRAbsoluteEncoder, thus improper implementations often lead to improper casting. Be careful.
     * @return whether the encoder has absolute funcionality
     */
    public default boolean isAbsolute()
    {
        return false;
    }
    /**
     * Resets the position of the encoder. Useful for relative applications.
     * @param position of the encoder which is affected by the conversion factor.
     */
    public void setEncoderPosition(double position);
    /**
     * Sets the position using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param position of the encoder which is affected by the conversion factor.
     */
    public void setSimulationPosition(double position);
    /**
     * Adds to the position using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param position of the encoder which is affected by the conversion factor.
     */
    public void addSimulationPosition(double position);
    /**
     * Sets the velocity using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param velocity of the encoder which is affected by the conversion factor.
     */
    public void setSimulationVelocity(double velocity);
}