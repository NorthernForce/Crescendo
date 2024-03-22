package org.northernforce.gyros;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.I2C.Port;

public class NFRNavX extends AHRS implements NFRGyro
{
    private final int simulationHandle;
    private final SimDouble headingSimulation;
    /** Creates a new NavX. Assumes default port (on top of the roborio) */
    public NFRNavX()
    {
        if (RobotBase.isSimulation())
        {
            simulationHandle = SimDeviceDataJNI.getSimDeviceHandle("navX-Sensor[0]");
            headingSimulation = new SimDouble(SimDeviceDataJNI.getSimValueHandle(simulationHandle, "Yaw"));
        }
        else
        {
            simulationHandle = -1;
            headingSimulation = null;
        }
    }
    /**
     * Creates a new NavX.
     * @param port the port the NavX is plugged into.
     */
    public NFRNavX(Port port)
    {
        super(port);
        if (RobotBase.isSimulation())
        {
            simulationHandle = SimDeviceDataJNI.getSimDeviceHandle("navX-Sensor[0]");
            headingSimulation = new SimDouble(SimDeviceDataJNI.getSimValueHandle(simulationHandle, "Yaw"));
        }
        else
        {
            simulationHandle = -1;
            headingSimulation = null;
        }
    }
    /**
     * Gets the current yaw angle of the gyroscope. Applies offset.
     * @return a Rotation2d of the gyro yaw. It is continuous
     */
    @Override
    public Rotation2d getGyroYaw()
    {
        return Rotation2d.fromDegrees(-getYaw());
    }
    /**
     * Gets the current rotational state of the gyroscope.
     * @return a Rotation3d representing the roll, pitch, and yaw of the gyroscope. It is continuous
     */
    @Override
    public Rotation3d getGyroAngle()
    {
        return new Rotation3d(
            getRoll(),
            getPitch(),
            getYaw()
        );
    }
    /**
     * Gets the current velocities recorded by the gyroscope. May be less accurate than odometry.
     * @return a Translation 3d containing the forward movement, left to right movement, and the vertical velocity
     */
    @Override
    public Translation3d getGyroVelocity()
    {
        return new Translation3d(
            getVelocityX(),
            getVelocityY(),
            getVelocityZ()
        );
    }
    /**
     * Gets the current rotational offset. May be permanently stored. Depends on the gyroscope.
     * @return a Rotation2d offset added to the raw yaw returned by the gyroscope
     */
    @Override
    public Rotation2d getYawOffset()
    {
        return Rotation2d.fromDegrees(getAngleAdjustment());
    }
    /**
     * Sets the current rotational offset. May be permantely stored. Depends on the gyro.
     * @param offset a Rotation2d offset to be added for every call to getGyroYaw() or the yaw value in getGyroAngle()
     */
    @Override
    public void setYawOffset(Rotation2d offset)
    {
        setAngleAdjustment(offset.getDegrees());
    }
    /**
     * Adds to the simulation API's yaw value. Do not use when not in simulation. You do not need to factor in
     * yaw offset.
     * @param deltaYaw Rotation2d object that is added to the current simulation yaw.
     */
    @Override
    public void addSimulationYaw(Rotation2d deltaYaw)
    {
        headingSimulation.set(
            -MathUtil.inputModulus(getGyroYaw().plus(deltaYaw).getDegrees(), -180, 180) - getAngleAdjustment()
        );
    }
    /**
     * Empty calibration for now.
     */
    @Override
    public void calibrate()
    {
    }
}