package frc.robot.commands;

import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class NFRSwerveDriveFollowPath extends Command
{
    protected final NFRSwerveDrive drive;
    protected PathPlannerTrajectory trajectory;
    protected final PathPlannerPath path;
    protected final Timer timer;
    protected final Supplier<Pose2d> poseSupplier;
    protected final PPHolonomicDriveController controller;
    protected final Supplier<Rotation2d> desiredRotation;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final double tolerance;
    public NFRSwerveDriveFollowPath(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PathPlannerPath path, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, Supplier<Rotation2d> desiredRotation, double tolerance)
    {
        this.drive = drive;
        this.path = path;
        timer = new Timer();
        this.poseSupplier = poseSupplier;
        this.controller = controller;
        this.desiredRotation = desiredRotation;
        this.setStateCommands = setStateCommands;
        this.tolerance = tolerance;
    }
    @Override
    public void initialize()
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
        trajectory = path.getTrajectory(drive.getChassisSpeeds(), drive.getRotation());
    }
    @Override
    public void execute()
    {
        ChassisSpeeds targetChassisSpeeds = controller.calculateRobotRelativeSpeeds(poseSupplier.get(), trajectory.sample(timer.get()));
        SwerveModuleState[] states = drive.toModuleStates(targetChassisSpeeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(states[i]);
        }
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.cancel();
        }
    }
    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(trajectory.getTotalTimeSeconds()) && poseSupplier.get().getTranslation()
            .getDistance(trajectory.getEndState().positionMeters) < tolerance;
    }
}
