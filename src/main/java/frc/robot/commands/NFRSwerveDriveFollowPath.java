package frc.robot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;

import edu.wpi.first.math.MathUtil;
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
    protected final ArrayList<EventMarker> untriggeredMarkers;
    protected final Map<Command, Boolean> currentEventCommands = new HashMap<>();
    protected final boolean ignoreCommands;
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
        PPHolonomicDriveController controller, Supplier<Rotation2d> desiredRotation, double tolerance, BooleanSupplier shouldFlipPath, boolean ignoreCommands)
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
        this.untriggeredMarkers = new ArrayList<>();
        controller.setEnabled(true);
        this.ignoreCommands = ignoreCommands;
        addRequirements(drive);
    }
    @Override
    public void initialize()
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
        var currentPose = poseSupplier.get();
        if (shouldFlipPath.getAsBoolean())
        {
            trajectory = path.flipPath().getTrajectory(drive.getChassisSpeeds(), Rotation2d.fromRadians(
                MathUtil.angleModulus(drive.getRotation().getRadians())));
        }
        else
        {
            trajectory = path.getTrajectory(drive.getChassisSpeeds(), Rotation2d.fromRadians(
                MathUtil.angleModulus(drive.getRotation().getRadians())));
        }
        controller.reset(currentPose, drive.getChassisSpeeds());
        timer.restart();
        untriggeredMarkers.clear();
        if (!ignoreCommands)
        {
            untriggeredMarkers.addAll(path.getEventMarkers());
        }
        currentEventCommands.clear();
        for (EventMarker marker : path.getEventMarkers())
        {
            marker.reset(currentPose);
        }
    }
    @Override
    public void execute()
    {
        var currentPose = poseSupplier.get();
        ChassisSpeeds targetChassisSpeeds = controller.calculateRobotRelativeSpeeds(currentPose, trajectory.sample(timer.get()));
        targetChassisSpeeds.omegaRadiansPerSecond = MathUtil.applyDeadband(targetChassisSpeeds.omegaRadiansPerSecond, 0.1);
        SwerveModuleState[] states = drive.toModuleStates(targetChassisSpeeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(states[i]);
        }
        for (Map.Entry<Command, Boolean> runningCommand : currentEventCommands.entrySet()) {
            if (!runningCommand.getValue())
            {
                continue;
            }
            runningCommand.getKey().execute();
            if (runningCommand.getKey().isFinished())
            {
                runningCommand.getKey().end(false);
                runningCommand.setValue(false);
            }
        }
        List<EventMarker> toTrigger = untriggeredMarkers.stream()
                .filter(marker -> marker.shouldTrigger(currentPose))
                .collect(Collectors.toList());
        untriggeredMarkers.removeAll(toTrigger);
        for (EventMarker marker : toTrigger)
        {
            for (var runningCommand : currentEventCommands.entrySet())
            {
                if (!runningCommand.getValue())
                {
                    continue;
                }
                if (!Collections.disjoint(runningCommand.getKey().getRequirements(), marker.getCommand().getRequirements())) {
                    runningCommand.getKey().end(true);
                    runningCommand.setValue(false);
                }
            }
            marker.getCommand().initialize();
            currentEventCommands.put(marker.getCommand(), true);
        }
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.cancel();
        }
        for (Map.Entry<Command, Boolean> runningCommand : currentEventCommands.entrySet())
        {
            if (runningCommand.getValue())
            {
                runningCommand.getKey().end(true);
            }
        }
    }
    @Override
    public boolean isFinished()
    {
        for (var runningCommand : currentEventCommands.entrySet())
        {
            if (runningCommand.getValue())
            {
                if (!runningCommand.getKey().isFinished())
                {
                    return false;
                }
            }
        }
        return timer.hasElapsed(trajectory.getTotalTimeSeconds());
    }
}
