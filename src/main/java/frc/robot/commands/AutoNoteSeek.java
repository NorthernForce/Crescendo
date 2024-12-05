package frc.robot.commands;

import org.northernforce.commands.NFRSwerveModuleSetState;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.Xavier;

public class AutoNoteSeek extends Command
{
    SwerveDrive drive;
    NFRSwerveModuleSetState[] setStateCommands;
    Xavier xavier;
    PIDController pid;
    Timer notelessTimer;
    /**
     * create a new AutoNoteSeek command
     * @param drive the swerve drive of the robot
     * @param setStateCommands commands to set states of swerve modules
     * @param xavier the xavier running note detection
     */
    public AutoNoteSeek(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Xavier xavier)
    {
        addRequirements(drive, xavier);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.xavier = xavier;
        this.pid = CrabbyConstants.XavierConstants.noteRotationPID;
    }
    @Override
    public void initialize()
    {
        notelessTimer.restart();
        pid.reset();
    }
    @Override
    public void execute()
    {
        float radian = xavier.getYawRadians();
        ChassisSpeeds speeds = new ChassisSpeeds();
        if (Float.isNaN(radian))
        {
            pid.reset();
        }
        else
        {
            notelessTimer.reset();
            speeds.vxMetersPerSecond = CrabbyConstants.XavierConstants.autoDriveSpeed;
            speeds.omegaRadiansPerSecond = pid.calculate(radian);
        }
        drive.drive(speeds, setStateCommands, true, false);
    }
    @Override
    public void end(boolean interrupted)
    {
        notelessTimer.stop();
    }
    @Override
    public boolean isFinished()
    {
        return notelessTimer.hasElapsed(CrabbyConstants.XavierConstants.noNoteDeadline);
    }
}
