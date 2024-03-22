package org.northernforce.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.drive.NFRTankDrive;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Drives a tank drive with inputs from a joystick.
 */
public class NFRTankDriveWithJoystick extends Command
{
    protected final NFRTankDrive drive;
    protected final DoubleSupplier xSupplier, thetaSupplier;
    protected final boolean useClosedLoopControl;
    protected final int pidSlot;
    /**
     * Creates a new NFRTankDriveWithJoystick.
     * @param drive the drivetrain subsystem.
     * @param xSupplier the supplier for the linear x velocity (forward/back).
     * @param thetaSupplier the supplier for the angular theta velocity (left/right);
     */
    public NFRTankDriveWithJoystick(NFRTankDrive drive, DoubleSupplier xSupplier, DoubleSupplier thetaSupplier)
    {
        addRequirements(drive);
        this.drive = drive;
        this.xSupplier = xSupplier;
        this.thetaSupplier = thetaSupplier;
        this.useClosedLoopControl = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRTankDriveWithJoystick. Uses closed loop control.
     * @param drive the drivetrain subsystem.
     * @param xSupplier the supplier for the linear x velocity (forward/back).
     * @param thetaSupplier the supplier for the angular theta velocity (left/right);
     * @param pidSlot the pid slot index for velocity control.
     */
    public NFRTankDriveWithJoystick(NFRTankDrive drive, DoubleSupplier xSupplier, DoubleSupplier thetaSupplier,
        int pidSlot)
    {
        addRequirements(drive);
        this.drive = drive;
        this.xSupplier = xSupplier;
        this.thetaSupplier = thetaSupplier;
        this.useClosedLoopControl = true;
        this.pidSlot = pidSlot;
    }
    /**
     * Sets the speeds of the chassis to the inputs from the xSupplier and thetaSupplier.
     */
    @Override
    public void execute()
    {
        ChassisSpeeds speeds = new ChassisSpeeds(xSupplier.getAsDouble(), 0, thetaSupplier.getAsDouble());
        DifferentialDriveWheelSpeeds wheelSpeeds = drive.toWheelSpeeds(speeds);
        if (useClosedLoopControl)
        {
            wheelSpeeds = drive.scaleSpeeds(wheelSpeeds);
            drive.driveClosedLoop(wheelSpeeds, pidSlot);
        }
        else
        {
            drive.driveOpenLoop(wheelSpeeds);
        }
    }
}