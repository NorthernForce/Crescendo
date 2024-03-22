package org.northernforce.util;

import java.util.Map;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * A robot container should contain all subsystems and states. Has various utility functions.
 */
public interface NFRRobotContainer
{
    /**
     * Binds the commands from subsystems to the HID's. Check the actual implementation to see which HID's are supported.
     * @param driverHID the driver controller.
     * @param manipulatorHID the manipulator controller.
     */
    public void bindOI(GenericHID driverHID, GenericHID manipulatorHID);
    /**
     * Runs periodically (every 20 ms) regardless of mode.
     */
    public default void periodic()
    {
    }
    /**
     * Runs periodically (every 20 ms) in only teleop.
     */
    public default void teleopPeroidic()
    {
    }
    /**
     * Runs periodically (every 20 ms) in only autonomous.
     */
    public default void autonomousPeriodic()
    {
    }
    /**
     * Returns a list of autonomous commands. First is the default command.
     * @return all of the autonomous options
     */
    public Map<String, Command> getAutonomousOptions();
    /**
     * Returns a list of the starting locations. First is the default starting location
     * @return all of the starting locations
     */
    public Map<String, Pose2d> getStartingLocations();
    /**
     * Gets the default autonomous option in case none is selected.
     * @return the default autonomous.
     */
    public Pair<String, Command> getDefaultAutonomous();
    public void setInitialPose(Pose2d pose);
}
