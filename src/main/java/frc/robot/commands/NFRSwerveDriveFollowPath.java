package frc.robot.commands;

import java.util.function.BooleanSupplier;
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

/**
 * A command to follow a path with a NFRSwerveDrive.
 */
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
    protected final BooleanSupplier shouldFlipPath;
    /**
     * Creates a new NFRSwerveDriveFollowPath
     * @param drive the drive subsystem
     * @param setStateCommands the set state commands
     * @param path the path to follow. Should be made for the blue alliance
     * @param poseSupplier the supplier of the pose estimation
     * @param controller the controller to follow the pose
     * @param desiredRotation the desired rotation to follow
     * @param tolerance the tolerance at the end
     * @param shouldFlipPath whether to flip the path (ie. if red alliance)
     */
    public NFRSwerveDriveFollowPath(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PathPlannerPath path, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, Supplier<Rotation2d> desiredRotation, double tolerance, BooleanSupplier shouldFlipPath)
    {
        this.drive = drive;
        this.path = path;
        timer = new Timer();
        this.poseSupplier = poseSupplier;
        this.controller = controller;
        this.desiredRotation = desiredRotation;
        this.setStateCommands = setStateCommands;
        this.tolerance = tolerance;
        this.shouldFlipPath = shouldFlipPath;
    }
    @Override
    public void initialize()
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
        if (shouldFlipPath.getAsBoolean())
        {
            trajectory = path.flipPath().getTrajectory(drive.getChassisSpeeds(), drive.getRotation());
        }
        else
        {
            trajectory = path.getTrajectory(drive.getChassisSpeeds(), drive.getRotation());
        }
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
