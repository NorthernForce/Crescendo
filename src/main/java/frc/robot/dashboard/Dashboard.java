package frc.robot.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.utils.AutonomousRoutine;

/**
 * This is the base class for any Dashboard interface
 */
public abstract class Dashboard
{
    protected final NetworkTable table;
    protected final SendableChooser<AutonomousRoutine> autoChooser;
    protected final Map<String, SendableBuilderImpl> sendables;
    protected final Notifier notifier = new Notifier(this::periodic);
    /**
     * Creates a new Dashboard
     * @param tablePath the path to the table
     */
    public Dashboard(String tablePath)
    {
        table = NetworkTableInstance.getDefault().getTable(tablePath);
        autoChooser = new SendableChooser<>();
        sendables = new HashMap<>();
        notifier.startPeriodic(0.02);
    }
    /**
     * Adds a sendable to the dashboard
     * @param name the name of the sendable
     * @param sendable the sendable to publish
     */
    public void addSendable(String name, Sendable sendable)
    {
        NetworkTable subTable = table.getSubTable(name);
        SendableBuilderImpl builder = new SendableBuilderImpl();
        builder.setTable(subTable);
        SendableRegistry.publish(sendable, builder);
        builder.startListeners();
        subTable.getEntry(".name").setString(name);
        sendables.put(name, builder);
    }
    /**
     * Displays autonomous routines with a name. Intended for internal use
     * @param name the name of the SendableChooser
     * @param routines the routines to choose from
     */
    protected void displayAutonomousRoutines(String name, List<AutonomousRoutine> routines)
    {
        if (routines.size() != 0)
        {
            autoChooser.addOption(routines.get(0).name(), routines.get(0));
        }
        for (int i = 1; i < routines.size(); i++)
        {
            autoChooser.addOption(routines.get(i).name(), routines.get(i));
        }
        addSendable(name, autoChooser);
    }
    /**
     * Displays the autonomous routines
     * @param routines the routines to choose from
     */
    public abstract void displayAutonomousRoutines(List<AutonomousRoutine> routines);
    /**
     * Gets the currently selected autonomous routine
     * @return the selected AutonomousRoutine
     */
    public AutonomousRoutine getSelectedRoutine()
    {
        return autoChooser.getSelected();
    }
    public void periodic()
    {
        for (var sendable : sendables.entrySet())
        {
            sendable.getValue().update();
        }
    }
}
