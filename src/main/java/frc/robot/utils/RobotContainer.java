package frc.robot.utils;

import java.util.List;

import org.northernforce.util.NFRRobotContainer;

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
}
