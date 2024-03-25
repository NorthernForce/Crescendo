package frc.robot.commands;

import org.northernforce.commands.NFRSwerveModuleSetState;

import com.pathplanner.lib.commands.PathfindHolonomic;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.FieldConstants;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.SwerveDrive;

public class AlignToAmp extends PathfindHolonomic
{
    protected final SwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final boolean optimize;
    /**
     * Creates a new AlignToAmp
     * @param drive the swerve drive subsystem
     * @param setStateCommands the commands to set the state of each swerve module
     * @param optimize whether to optimize each swerve module (cut to the quickest possible state)
     */
    public AlignToAmp(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, boolean optimize)
    {
        super(DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? FieldConstants.AmpPositions.redAmp : FieldConstants.AmpPositions.blueAmp,
            CrabbyConstants.DriveConstants.constraints, drive::getEstimatedPose, drive::getChassisSpeeds, speeds -> drive.drive(speeds, setStateCommands, optimize, false),
            CrabbyConstants.DriveConstants.holonomicConfig, drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.optimize = optimize;
    }
    @Override
    public void initialize()
    {
        for (var command : setStateCommands)
        {
            command.schedule();
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