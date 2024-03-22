package org.northernforce.commands;

import org.northernforce.subsystems.drive.NFRTankDrive;

import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Stops the tank chassis.
 */
public class NFRTankDriveStop extends Command
{
    protected final NFRTankDrive drive;
    protected final double tolerance;
    protected final boolean useClosedLoopBrake;
    protected final int pidSlot;
    /**
     * Stops the tank chassis.
     * @param drive the drivetrain subsystem.
     * @param tolerance the tolerance for when the command is over.
     */
    public NFRTankDriveStop(NFRTankDrive drive, double tolerance)
    {
        addRequirements(drive);
        this.drive = drive;
        this.tolerance = tolerance;
        this.useClosedLoopBrake = false;
        this.pidSlot = -1;
    }
    /**
     * Stops the tank chassis. Uses closed-loop control.
     * @param drive the drivetrain subsystem.
     * @param tolerance the tolerance for when the command is over.
     * @param pidSlot the pid slot for velocity closed-loop control.
     */
    public NFRTankDriveStop(NFRTankDrive drive, double tolerance, int pidSlot)
    {
        addRequirements(drive);
        this.drive = drive;
        this.tolerance = tolerance;
        this.useClosedLoopBrake = true;
        this.pidSlot = pidSlot;
    }
    /**
     * If using closed-loop control, sets the integrated pid in the motors to stop movement.
     */
    @Override
    public void initialize()
    {
        if (useClosedLoopBrake)
        {
            drive.driveClosedLoop(new DifferentialDriveWheelSpeeds(), pidSlot);
        }
    }
    /**
     * If not using closed-loop control, sets the input to the motors to be zero.
     */
    @Override
    public void execute()
    {
        if (!useClosedLoopBrake)
        {
            drive.driveOpenLoop(new DifferentialDriveWheelSpeeds());
        }
    }
    /**
     * Returns whether the motors' speeds are less than or equal to the tolerance.
     * @return within tolerance of stopping
     */
    @Override
    public boolean isFinished()
    {
        var speeds = drive.getWheelSpeeds();
        return Math.abs(speeds.leftMetersPerSecond) <= tolerance && Math.abs(speeds.rightMetersPerSecond) <= tolerance;
    }
}