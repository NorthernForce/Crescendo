package org.northernforce.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Command responsible for driving a swerve drive via joystick inputs.
 */
public class NFRSwerveDriveWithJoystick extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final DoubleSupplier xSupplier, ySupplier, thetaSupplier;
    protected final boolean optimize, fieldRelative;
    /**
     * Creates a new NFRSwerveDriveWithJoystick
     * @param drive the swerve drive instance
     * @param setStateCommands the list of commands that will be used to set the state of the swerve drive.
     * @param xSupplier the x supplier for the ChassisSpeeds.
     * @param ySupplier the y supplier for the ChassisSpeeds.
     * @param thetaSupplier the theta supplier for the ChassisSpeeds.
     * @param optimize whether to optimize swerve module positions.
     * @param fieldRelative whether to use field relative driving (recommended if gyro works).
     */
    public NFRSwerveDriveWithJoystick(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier, boolean optimize,
        boolean fieldRelative)
    {
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.thetaSupplier = thetaSupplier;
        this.optimize = optimize;
        this.fieldRelative = fieldRelative;
    }
    /**
     * Initializes the command and schedules the swerve module set state commands.
     */
    @Override
    public void initialize()
    {
        for (NFRSwerveModuleSetState command : setStateCommands)
        {
            command.schedule();
        }
    }
    /**
     * Calculates inverse kinematics based on the supplied joystick inputs and feeds it to the setState commands.
     */
    @Override
    public void execute()
    {
        ChassisSpeeds speeds = new ChassisSpeeds(xSupplier.getAsDouble(), ySupplier.getAsDouble(), thetaSupplier.getAsDouble());
        if (fieldRelative)
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, drive.getRotation());
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i],
                drive.getModules()[i].getRotation()) : states[i]);
        }
    }
    /**
     * Stops all of the setState commands.
     */
    @Override
    public void end(boolean interrupted)
    {
        for (NFRSwerveModuleSetState command : setStateCommands)
        {
            command.cancel();
        }
    }
}