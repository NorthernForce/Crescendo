package frc.robot.utils;

import java.util.List;
import java.util.Map;

import org.northernforce.util.NFRRobotContainer;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.dashboard.Dashboard;

/**
 * A robot container should contain all subsystems and states. Has various utility functions.
 * This specific container interface adds the new functionality provided by getAutonomousRoutines().
 */
public interface RobotContainer extends NFRRobotContainer
{
    /**
     * Gets a list of autonomous routine. Each routine is a set of a name, starting pose, and command to run.
     * @return list of frc.robot.utils.AutonomousRoutine
     */
    public List<AutonomousRoutine> getAutonomousRoutines();
    /**
     * Gets the dashboard used by the robot container
     * @return Class that inherits from Dashboard
     */
    public Dashboard getDashboard();
    /**
     * Binds the operator interface
     */
    public void bindOI();
    @Deprecated
    @Override
    public void bindOI(GenericHID driverHID, GenericHID manipulatorHID);
    @Deprecated
    @Override
    public Map<String, Command> getAutonomousOptions();
    @Override
    @Deprecated
    public Map<String, Pose2d> getStartingLocations();
    @Override
    @Deprecated
    public Pair<String, Command> getDefaultAutonomous();
}
