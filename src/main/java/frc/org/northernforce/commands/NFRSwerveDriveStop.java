package org.northernforce.commands;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Points the wheels inwards, effectively stopping the swerve drive.
 */
public class NFRSwerveDriveStop extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final boolean optimize;
    /**
     * Creates a new NFRSwerveDriveStop
     * @param drive the swerve drive to stop
     * @param setStateCommands the set state commands for the modules
     * @param optimize whether to optimize (should be true)
     */
    public NFRSwerveDriveStop(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, boolean optimize)
    {
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.optimize = optimize;
    }
    @Override
    public void initialize()
    {
        for (NFRSwerveModuleSetState command : setStateCommands)
        {
            command.schedule();
        }
        SwerveModuleState[] states = drive.getStopState();
        for (int i = 0; i < setStateCommands.length; i++)
        {
            setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i],
                drive.getModules()[i].getRotation()) : states[i]);
        }
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var command : setStateCommands)
        {
            command.cancel();
        }
    }
}
