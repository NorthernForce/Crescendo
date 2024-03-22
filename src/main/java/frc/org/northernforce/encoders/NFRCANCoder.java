package org.northernforce.encoders;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.sim.CANcoderSimState;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * The NFRCANCoder is an interface for the CTRE CANCoder. This interface inherits from NFRAbsoluteEncoder.
 */
public class NFRCANCoder implements NFRAbsoluteEncoder
{
    private final CANcoder cancoder;
    private final CANcoderSimState simState;
    private double factor = 1;
    private boolean useAbsoluteMeasurements = true;
    /**
     * Creates a new NFRCANCoder.
     * @param id the ID of the device on the canbus.
     */
    public NFRCANCoder(int id)
    {
        cancoder = new CANcoder(id);
        if (RobotBase.isSimulation())
        {
            simState = cancoder.getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Creates a new NFRCANCoder.
     * @param configuration The configuration to load onto the CANCoder.
     * @param id the ID of the device on the canbus.
     */
    public NFRCANCoder(CANcoderConfiguration configuration, int id)
    {
        cancoder = new CANcoder(id);
        cancoder.getConfigurator().apply(configuration);
        if (RobotBase.isSimulation())
        {
            simState = cancoder.getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Creates a new NFRCANCoder.
     * @param canbus the canbus of the cancoder
     * @param id the ID of the device on the canbus.
     */
    public NFRCANCoder(String canbus, int id)
    {
        cancoder = new CANcoder(id, canbus);
        if (RobotBase.isSimulation())
        {
            simState = cancoder.getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Creates a new NFRCANCoder.
     * @param canbus the canbus of the cancoder
     * @param configuration The configuration to load onto the CANCoder.
     * @param id the ID of the device on the canbus.
     */
    public NFRCANCoder(String canbus, CANcoderConfiguration configuration, int id)
    {
        cancoder = new CANcoder(id, canbus);
        cancoder.getConfigurator().apply(configuration);
        if (RobotBase.isSimulation())
        {
            simState = cancoder.getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Checks whether this cancoder is present on the canbus.
     */
    @Override
    public boolean isPresent()
    {
        return cancoder.getVersion().getValue() != -1;
    }
    /**
     * Sets the inversion of the CANCoder.
     * @param inverted true is clockwise, false is counter-clockwise.
     */
    @Override
    public void setInverted(boolean inverted)
    {
        CANcoderConfiguration config = new CANcoderConfiguration();
        cancoder.getConfigurator().refresh(config);
        config.MagnetSensor.SensorDirection = inverted ? SensorDirectionValue.Clockwise_Positive
            : SensorDirectionValue.CounterClockwise_Positive;
        cancoder.getConfigurator().apply(config);
    }
    /**
     * Sets the conversion factor for the encoder measurements.
     * @param factor 1 = 1 rotation.
     */
    @Override
    public void setConversionFactor(double factor)
    {
        this.factor = factor;
    }
    /**
     * Returns the conversion factor.
     * @return the conversion factor.
     */
    @Override
    public double getConversionFactor()
    {
        return factor;
    }
    /**
     * Sets the position of the encoder.
     * @param position in units determined by conversion factor.
     */
    @Override
    public void setEncoderPosition(double position)
    {
        cancoder.setPosition(position / factor * 180.0);
    }
    /**
     * Sets the simulation position of the encoder. Only use in simulation.
     * @param position in units determined by conversion factor.
     */
    @Override
    public void setSimulationPosition(double position)
    {
        simState.setRawPosition(position / factor);
    }
    /**
     * Adds to the simulation position of the encoder. Only use in simulation.
     * @param position in units determined by conversion factor.
     */
    @Override
    public void addSimulationPosition(double position)
    {
        simState.addPosition(position / factor);
    }
    /**
     * Sets the simulation velocity of the encoder. Only use in simulation.
     * @param velocity in units determined by conversion factor.
     */
    @Override
    public void setSimulationVelocity(double velocity)
    {
        simState.setVelocity(velocity / factor);
    }
    /**
     * Gets the velocity of the absolute sensor.
     * @return velocity in units determined by conversion factor.
     */
    @Override
    public double getAbsoluteVelocity()
    {
        return cancoder.getVelocity().getValue() * factor;
    }
    /**
     * Sets whether the absolute encoder is inverted. Also sets the relative encoder's inversion.
     * @param inverted true is clockwise, false is counter-clockwise.
     */
    @Override
    public void setAbsoluteInverted(boolean inverted)
    {
        setInverted(inverted);
    }
    /**
     * Sets the absolute offset of the CANCoder.
     * @param offset in units determined by conversion factor. 
     */
    @Override
    public void setAbsoluteOffset(double offset)
    {
        CANcoderConfiguration config = new CANcoderConfiguration();
        cancoder.getConfigurator().refresh(config);
        config.MagnetSensor.MagnetOffset = offset / factor;
        cancoder.getConfigurator().apply(config);
    }
    /**
     * This is a permanant configuration, and thus will be saved between boots.
     * @return offset in units based on absolute conversion factor.
     */
    @Override
    public double getAbsoluteOffset()
    {
        CANcoderConfiguration config = new CANcoderConfiguration();
        cancoder.getConfigurator().refresh(config);
        return config.MagnetSensor.MagnetOffset * factor;
    }
    /**
     * Sets the conversion factor for the absolute sensor. Also affects the relative encoder.
     * @param factor 1 = 1 rotation.
     */
    @Override
    public void setAbsoluteConversionFactor(double factor)
    {
        setConversionFactor(factor);
    }
    /**
     * Sets whether getPosition returns absolute measurements. True by default.
     * @param useAbsoluteMeasurements whether getPosition returns absolute measurements.
     */
    @Override
    public void setUseAbsoluteMeasurements(boolean useAbsoluteMeasurements)
    {
        this.useAbsoluteMeasurements = useAbsoluteMeasurements;
    }
    /**
     * Sets the absolute simulation position of the encoder. Only use in simulation.
     * @param position in units determined by conversion factor.
     */
    @Override
    public void setAbsoluteSimulationPosition(double position)
    {
        setSimulationPosition(position);
    }
    /**
     * Adds to the absolute simulation position of the encoder. Only use in simulation.
     * @param position in units determined by conversion factor.
     */
    @Override
    public void addAbsoluteSimulationPosition(double position)
    {
        addSimulationPosition(position);
    }
    /**
     * Sets the absolute simulation velocity of the encoder. Only use in simulation.
     * @param velocity in units determined by conversion factor.
     */
    @Override
    public void setAbsoluteSimulationVelocity(double velocity)
    {
        setSimulationVelocity(velocity);
    }
    /**
     * Gets the position of the encoder.
     * @return position in units determined by conversion factor.
     */
    @Override
    public double getPosition()
    {
        if (useAbsoluteMeasurements)
        {
            return cancoder.getAbsolutePosition().getValue() * factor;
        }
        else
        {
            return cancoder.getPositionSinceBoot().getValue() * factor;
        }
    }
    /**
     * Gets the velocity of the CANCoder.
     * @return velocity in units determined by conversion factor.
     */
    @Override
    public double getVelocity()
    {
        return cancoder.getVelocity().getValue() * factor;
    }
    /**
     * Gets the absolute position of the encoder.
     * @return position in units determined by conversion factor.
     */
    @Override
    public double getAbsolutePosition()
    {
        return cancoder.getAbsolutePosition().getValue() * factor;
    }
    /**
     * Gets the CANCoder that this class contains.
     * @return the actual cancoder.
     */
    public CANcoder getEncoder()
    {
        return cancoder;
    }
    /**
     * Sets the range of the CANCoder output.
     * @param negative true means -0.5 to 0.5, false means 0 to 1.
     */
    public void setRange(boolean negative)
    {
        CANcoderConfiguration config = new CANcoderConfiguration();
        cancoder.getConfigurator().refresh(config);
        config.MagnetSensor.AbsoluteSensorRange = negative ? AbsoluteSensorRangeValue.Signed_PlusMinusHalf : AbsoluteSensorRangeValue.Unsigned_0To1;
        cancoder.getConfigurator().apply(config);
    }
}
