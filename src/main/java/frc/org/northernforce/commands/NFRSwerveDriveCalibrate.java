package org.northernforce.commands;

import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

/**
 * NFRSwerveDriveCalibrate resets the all of the modules in the swerve drive.
 */
public class NFRSwerveDriveCalibrate extends ParallelCommandGroup
{
    /**
     * Creates a new NFRSwerveDriveCalibrate.
     * @param drive the swerve drive subsystem
     */
    public NFRSwerveDriveCalibrate(NFRSwerveDrive drive)
    {
        addRequirements(drive);
        for (NFRSwerveModule module : drive.getModules())
        {
            addCommands(new NFRSwerveModuleCalibrate(module));
        }
    }
}