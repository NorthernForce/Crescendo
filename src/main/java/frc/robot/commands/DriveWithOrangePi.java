package frc.robot.commands;

import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.OrangePi;

public class DriveWithOrangePi extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final OrangePi orangePi;
    protected final Supplier<Pose2d> targetPose;
    protected Pose2d currentTarget;
    protected final double translationalTolerance;
    protected final Rotation2d rotationalTolerance;
    /**
     * Creates a new DriveWithOrangePi. This command will use Navigation2 on the orange pi to navigate to a pose.
     * @param drive the drivetrain subsystem
     * @param setStateCommands the commands to move to a pose (must accept m/s not duty cycle input)
     * @param orangePi the orange pi to use
     * @param targetPose the target pose that will be supplied when the command starts
     * @param translationalTolerance the translational tolerance in meters for the end of the command
     * @param rotationalTolerance the rotational tolerance for the end of the command
     */
    public DriveWithOrangePi(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, OrangePi orangePi, Supplier<Pose2d> targetPose,
        double translationalTolerance, Rotation2d rotationalTolerance)
    {
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.orangePi = orangePi;
        this.targetPose = targetPose;
        this.translationalTolerance = translationalTolerance;
        this.rotationalTolerance = rotationalTolerance;
        addRequirements(drive, orangePi);
    }
    @Override
    public void initialize()
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
        orangePi.sendTargetPose(currentTarget = targetPose.get());
    }
    @Override
    public void execute()
    {
        var commandTwist = orangePi.getCommandVelocity();
        ChassisSpeeds speeds = new ChassisSpeeds(commandTwist.dx, commandTwist.dy, commandTwist.dtheta);
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < setStateCommands.length; i++)
        {
            setStateCommands[i].setTargetState(SwerveModuleState.optimize(states[i], drive.getModules()[i].getRotation()));
        }
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.cancel();
        }
        if (interrupted)
        {
            orangePi.cancelTargetPose();
        }
    }
    @Override
    public boolean isFinished()
    {
        return orangePi.getPose().getTranslation().getDistance(currentTarget.getTranslation()) < translationalTolerance
            && Math.abs(orangePi.getPose().getRotation().minus(currentTarget.getRotation()).getRadians()) < rotationalTolerance.getRadians();
    }
}
