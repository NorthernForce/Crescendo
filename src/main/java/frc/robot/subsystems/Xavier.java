package frc.robot.subsystems;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.networktables.FloatArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Subsystem for note detection running on Xavier coprocessor
 */
public class Xavier extends NFRSubsystem
{
    /**
     * Configuration class for Xavier subsystem
     */
    public static class XavierConfiguration extends NFRSubsystemConfiguration
    {
        protected String tableName;
        /**
         * Creates a new XavierConfiguration
         * @param name name of subsystem
         * @param tableName name of table where the xavier's predictions are published
         */
        public XavierConfiguration(String name, String tableName)
        {
            super(name);
            this.tableName = tableName;
        }
    }
    protected final NetworkTableInstance instance;
    protected final NetworkTable table;
    protected final FloatArraySubscriber xRadSubscriber;
    /**
     * Create a new Xavier subsystem
     * @param config the configuration for this subsystem
     */
    public Xavier(XavierConfiguration config)
    {
        super(config);
        instance = NetworkTableInstance.getDefault();
        table = instance.getTable(config.tableName);
        xRadSubscriber = table.getFloatArrayTopic("notes_rad").subscribe(new float[0]);
    }
    /**
     * Gets an array of radians on the x axis relative to the center of robot of predicted notes
     * @return x radians of notes relative to center of robot
     */
    public float[] getRadians()
    {
        return xRadSubscriber.get();
    }
    /**
     * Gets whether the Xavier/Nano is connected or not
     * @return whether Xavier/Nano is connected or not
     */
    public boolean isConnected()
    {
        for (var connection : instance.getConnections())
        {
            if (connection.remote_id.contains("skynet"))
            {
                return true;
            }
        }
        return false;
    }
}
