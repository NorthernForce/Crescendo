package org.northernforce.commands;

import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * NFRSwerveModuleCalibrate is a command designed to calibrate a single swerve drive module
 */
public class NFRSwerveModuleCalibrate extends Command
{
    protected final NFRSwerveModule module;
    protected final Rotation2d angle;
    /**
     * Creates a new NFRSwerveModuleCalibrate.
     * @param module the module to be calibrated
     */
    public NFRSwerveModuleCalibrate(NFRSwerveModule module)
    {
        addRequirements(module);
        this.module = module;
        this.angle = new Rotation2d();
    }
    /**
     * Creates a new NFRSwerveModuleCalibrate.
     * @param module the module to be calibrated
     * @param angle the angle to reset to
     */
    public NFRSwerveModuleCalibrate(NFRSwerveModule module, Rotation2d angle)
    {
        addRequirements(module);
        this.module = module;
        this.angle = angle;
    }
    /**
     * Resets the angle of the module.
     */
    @Override
    public void initialize()
    {
        module.resetAngle(angle);
    }
    /**
     * Returns whether the command is finished
     * @return always true
     */
    @Override
    public boolean isFinished()
    {
        return true;
    }
}
