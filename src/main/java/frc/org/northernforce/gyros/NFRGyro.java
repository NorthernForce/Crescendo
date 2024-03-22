package org.northernforce.gyros;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

/**
 * This class provides a common interface for accessing gyros. This is mainly intended to be implemented by
 * NavX's and Pigeon2's.
 */
public interface NFRGyro
{
    /**
     * Gets the current yaw angle of the gyroscope. Applies offset.
     * @return a Rotation2d of the gyro yaw. May be continuous or between [-180, 180]
     */
    public Rotation2d getGyroYaw();
    /**
     * Gets the current rotational state of the gyroscope.
     * @return a Rotation3d representing the roll, pitch, and yaw of the gyroscope. Not guarenteed to be continuous.
     */
    public Rotation3d getGyroAngle();
    /**
     * Gets the current velocities recorded by the gyroscope. May be more or less accurate than odometry.
     * @return a Translation 3d containing the forward movement, left to right movement, and the vertical velocity
     */
    public Translation3d getGyroVelocity();
    /**
     * Gets the current rotational offset. May be permanently stored. Depends on the gyroscope.
     * @return a Rotation2d offset added to the raw yaw returned by the gyroscope
     */
    public Rotation2d getYawOffset();
    /**
     * Sets the current rotational offset. May be permantely stored. Depends on the gyro.
     * @param offset a Rotation2d offset to be added for every call to getGyroYaw() or the yaw value in getGyroAngle()
     */
    public void setYawOffset(Rotation2d offset);
    /**
     * Starts the calibration process for the gyroscope. Likely runs it asynchronsly, therefore isCalibrating should be
     * checked periodically.
     */
    public void calibrate();
    /**
     * Checks to see whether the gyroscope is calibrating at the moment.
     * @return true if calibrating, else false
     */
    public boolean isCalibrating();
    /**
     * Adds to the simulation API's yaw value. Do not use when not in simulation. You do not need to factor in
     * yaw offset.
     * @param deltaYaw Rotation2d object that is added to the current simulation yaw.
     */
    public void addSimulationYaw(Rotation2d deltaYaw);
}