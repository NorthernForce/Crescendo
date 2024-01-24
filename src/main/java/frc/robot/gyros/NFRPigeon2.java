package frc.robot.gyros;

import org.northernforce.gyros.NFRGyro;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.sim.Pigeon2SimState;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * This is a class that extends the capabilities of a CTRE Pigeon2 so that it is compatible with the NFRGyro interface.
 */
public class NFRPigeon2 extends Pigeon2 implements NFRGyro
{
    protected final Pigeon2SimState simState;
    /**
     * Constructs a new pigeon2 on the roborio bus
     * @param deviceId the id of the pigeon2
     */
    public NFRPigeon2(int deviceId)
    {
        super(deviceId);
        if (RobotBase.isSimulation())
        {
            simState = getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Constructs a new pigeon2 on the roborio bus
     * @param deviceId the id of the pigeon2
     * @param config the pigeon2 configuration
     */
    public NFRPigeon2(int deviceId, Pigeon2Configuration config)
    {
        super(deviceId);
        getConfigurator().apply(config);
        if (RobotBase.isSimulation())
        {
            simState = getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Constructs a new pigeon2
     * @param canbus the name of the pigeon2's canbus
     * @param deviceId the id of the pigeon2
     */
    public NFRPigeon2(String canbus, int deviceId)
    {
        super(deviceId, canbus);
        if (RobotBase.isSimulation())
        {
            simState = getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Constructs a new pigeon2
     * @param canbus the name of the pigeon2' canbus
     * @param deviceId the id of the pigeon2
     * @param config the pigeon2 configuration
     */
    public NFRPigeon2(String canbus, int deviceId, Pigeon2Configuration config)
    {
        super(deviceId, canbus);
        getConfigurator().apply(config);
        if (RobotBase.isSimulation())
        {
            simState = getSimState();
        }
        else
        {
            simState = null;
        }
    }
    /**
     * Gets the current yaw angle of the gyroscope. Applies offset.
     * @return a Rotation2d of the gyro yaw. May be continuous or between [-180, 180]
     */
    @Override
    public Rotation2d getGyroYaw()
    {
        return getRotation2d();
    }
    /**
     * Gets the current rotational state of the gyroscope.
     * @return a Rotation3d representing the roll, pitch, and yaw of the gyroscope. Not guarenteed to be continuous.
     */
    @Override
    public Rotation3d getGyroAngle()
    {
        return getRotation3d();
    }
    /**
     * Gets the current velocities recorded by the gyroscope. May be more or less accurate than odometry.
     * @return a Translation 3d containing the forward movement, left to right movement, and the vertical velocity
     */
    @Override
    public Translation3d getGyroVelocity()
    {
        return new Translation3d(0, 0, 0); // TODO
    }
    /**
     * Gets the current rotational offset. May be permanently stored. Depends on the gyroscope.
     * @return a Rotation2d offset added to the raw yaw returned by the gyroscope
     */
    @Override
    public Rotation2d getYawOffset()
    {
        Pigeon2Configuration config = new Pigeon2Configuration();
        getConfigurator().refresh(config);
        return Rotation2d.fromDegrees(config.MountPose.MountPoseYaw);
    }
    /**
     * Sets the current rotational offset. May be permantely stored. Depends on the gyro.
     * @param offset a Rotation2d offset to be added for every call to getGyroYaw() or the yaw value in getGyroAngle()
     */
    @Override
    public void setYawOffset(Rotation2d offset)
    {
        Pigeon2Configuration config = new Pigeon2Configuration();
        getConfigurator().refresh(config);
        config.MountPose.MountPoseYaw = offset.getDegrees();
        getConfigurator().apply(config);
    }
    /**
     * Starts the calibration process for the gyroscope. Likely runs it asynchronsly, therefore isCalibrating should be
     * checked periodically.
     */
    @Override
    public void calibrate()
    {
    }
    /**
     * Checks to see whether the gyroscope is calibrating at the moment.
     * @return true if calibrating, else false
     */
    @Override
    public boolean isCalibrating()
    {
        return false;
    }
    /**
     * Adds to the simulation API's yaw value. Do not use when not in simulation. You do not need to factor in
     * yaw offset.
     * @param deltaYaw Rotation2d object that is added to the current simulation yaw.
     */
    @Override
    public void addSimulationYaw(Rotation2d deltaYaw)
    {
        simState.addYaw(deltaYaw.getDegrees());
    }
}
