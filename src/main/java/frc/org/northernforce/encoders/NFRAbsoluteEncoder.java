package org.northernforce.encoders;

/**
 * A NFR Absolute Encoder is common interface that is meant to provide consistent funcionality between absolute
 * encoders. This allows for more modular code and such. However, this said, the NFRAbsoluteEncoder is not a type of encoder
 * itself, and you must a create the encoder as another encoder that inherits from NFRAbsoluteEncoder.
 */
public interface NFRAbsoluteEncoder extends NFREncoder
{
    /**
     * Returns whether the encoder has absolute position measurement capabilities. Does not share whether it inherits from
     * NFRAbsoluteEncoder, thus improper implementations often lead to improper casting. Be careful.
     * @return whether the encoder has absolute funcionality
     */
    @Override
    public default boolean isAbsolute()
    {
        return true;
    }
    /**
     * Gets the position recorded by the sensor. This will return the relative position for the absolute sensor.
     * This value will be inverted as specified, and will be affected by the conversion factor.
     * @return Position affected by conversion factor, rotations by default.
     */
    public double getAbsolutePosition();
    /**
     * Gets the velocity recorded by the sensor. This will return the velocity measured by the absolute sensor.
     * The value will be inverted as specified, and will be affected by the conversion factor.
     * @return Velocity affected by conversion factor, rotations per second by default.
     */
    public double getAbsoluteVelocity();
    /**
     * Sets the inversion of the encoder. This affects getVelocity and getPosition. Remember, however, this is all relative
     * to how the encoder is placed on the shaft.
     * @param inverted whether to invert the encoder readings
     */
    public void setAbsoluteInverted(boolean inverted);
    /**
     * Sets the offset of the absolute sensor. This is a permanant configuration, and thus will be saved between boots.
     * @param offset in units based on absolute conversion factor.
     */
    public void setAbsoluteOffset(double offset);
    /**
     * This is a permanant configuration, and thus will be saved between boots.
     * @return offset in units based on absolute conversion factor.
     */
    public double getAbsoluteOffset();
    /**
     * Sets the conversion factor that affects readings of the sensor. This is by default 1. 
     * @param factor the factor for measurements of velocity and position. 1 means 1 unit = 1 encoder rotation.
     */
    public void setAbsoluteConversionFactor(double factor);
    /**
     * Sets whether to use absolute measurements in functions inherited from NFREncoder such as getPosition and getVelocity.
     * @param useAbsoluteMeasurements whether to use absolute measurements
     */
    public void setUseAbsoluteMeasurements(boolean useAbsoluteMeasurements);
    /**
     * Sets the position using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param position of the encoder which is affected by the conversion factor.
     */
    public void setAbsoluteSimulationPosition(double position);
    /**
     * Adds to the position using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param position of the encoder which is affected by the conversion factor.
     */
    public void addAbsoluteSimulationPosition(double position);
    /**
     * Sets the velocity using the simulation API. Use solely in simulation as it can lead to undefined behavior on
     * a robot.
     * @param velocity of the encoder which is affected by the conversion factor.
     */
    public void setAbsoluteSimulationVelocity(double velocity);
}