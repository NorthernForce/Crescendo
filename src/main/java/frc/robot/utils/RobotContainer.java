package frc.robot.utils;

import java.util.List;

import org.northernforce.util.NFRRobotContainer;

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
}
