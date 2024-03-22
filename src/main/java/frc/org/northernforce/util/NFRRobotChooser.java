package org.northernforce.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * A robot chooser is a class that is meant for reading a file to select the robot.
 */
public class NFRRobotChooser
{
    protected final Supplier<NFRRobotContainer> defaultRobot;
    protected final Map<String, Supplier<NFRRobotContainer>> otherRobots;
    protected final String robotNamePath;
    /**
     * Creates a new robot chooser.
     * @param defaultRobot default robot container should the file be nonexistant. Should be competition bot for
     * purposes of fallbacks should anything happen to the roboRio.
     * @param otherRobots the map of other robots.
     * @param robotNamePath the path of where to find the file.
     */
    public NFRRobotChooser(Supplier<NFRRobotContainer> defaultRobot, Map<String, Supplier<NFRRobotContainer>> otherRobots,
        String robotNamePath)
    {
        this.defaultRobot = defaultRobot;
        this.otherRobots = otherRobots;
        this.robotNamePath = robotNamePath;
    }
    /**
     * Creates a new robot chooser. Looks to '/home/admin/robot_settings.txt' for robot name.
     * @param defaultRobot default robot container should the file be nonexistant. Should be competition bot for
     * purposes of fallbacks should anything happen to the roboRio.
     * @param otherRobots the map of other robots.
     */
    public NFRRobotChooser(Supplier<NFRRobotContainer> defaultRobot, Map<String, Supplier<NFRRobotContainer>> otherRobots)
    {
        this(defaultRobot, otherRobots, "/home/admin/robot_settings.txt");
    }
    /**
     * Gets the current robot container by reading the name from the specified file.
     * @return the robot container using one of the suppliers.
     */
    public NFRRobotContainer getNFRRobotContainer()
    {
        try
        {
            File file = new File(robotNamePath);
            Scanner scanner = new Scanner(file);
            String robotName = scanner.next();
            scanner.close();
            for (var entry : otherRobots.entrySet())
            {
                if (entry.getKey().equalsIgnoreCase(robotName)) return entry.getValue().get();
            }
            return defaultRobot.get();
        }
        catch (FileNotFoundException e)
        {
            return defaultRobot.get();
        }
    }
}