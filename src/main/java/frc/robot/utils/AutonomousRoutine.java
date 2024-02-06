package frc.robot.utils;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This is a container class for an autonomous routine.
 * @param name the name of the routine
 * @param startingPose the supplier for the pose that is loaded into odometry at the start
 * @param command the command to run for autonomous
 */
public record AutonomousRoutine(String name, Supplier<Pose2d> startingPose, Command command)
{
}
