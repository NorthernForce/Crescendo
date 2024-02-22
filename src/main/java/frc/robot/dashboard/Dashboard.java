package frc.robot.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;
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
    protected final HashMap<Integer, Alert> infos, warnings, errors;
    protected final StringArrayPublisher infoPublishers, warningPublishers, errorPublishers;
    protected int alertId = 0;
    /**
     * Creates a new Dashboard
     * @param tablePath the path to the table
     */
    public Dashboard(String tablePath)
    {
        table = NetworkTableInstance.getDefault().getTable(tablePath);
        autoChooser = new SendableChooser<>();
        sendables = new HashMap<>();
        infos = new HashMap<>();
        warnings = new HashMap<>();
        errors = new HashMap<>();
        infoPublishers = table.getStringArrayTopic("infos").publish();
        warningPublishers = table.getStringArrayTopic("warnings").publish();
        errorPublishers = table.getStringArrayTopic("errors").publish();
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
    /**
     * A class to store a message with an id
     * @param id unique integer id
     * @param message a simple message to be displayed
     */
    public record Alert(int id, String message)
    {
    }
    /**
     * Creates an alert with an incrementing id
     * @param message the content of the alert message
     * @return an {@link Alert} with a unique id and the provided message
     */
    public Alert createAlert(String message)
    {
        return new Alert(alertId++, message);
    }
    /**
     * Adds info to be displayed
     * @param alert the alert to be displayed as info
     */
    public void addInfo(Alert alert)
    {
        infos.put(alert.id, alert);
    }
    /**
     * Removes info
     * @param alert the alert to remove
     */
    public void removeInfo(Alert alert)
    {
        infos.remove(alert.id);
    }
    /**
     * Adds warning to be displayed
     * @param alert the alert to be displayed as warning
     */
    public void addWarning(Alert alert)
    {
        warnings.put(alert.id, alert);
    }
    /**
     * Removes warning
     * @param alert the alert to remove
     */
    public void removeWarning(Alert alert)
    {
        warnings.remove(alert.id);
    }
    /**
     * Adds error to be displayed
     * @param alert the alert to be displayed as error
     */
    public void addError(Alert alert)
    {
        errors.put(alert.id, alert);
    }
    /**
     * Removes error
     * @param alert the alert to remove
     */
    public void removeError(Alert alert)
    {
        errors.remove(alert.id);
    }
    public void periodic()
    {
        for (var sendable : sendables.entrySet())
        {
            sendable.getValue().update();
        }
        String[] infoStrings = new String[infos.size()];
        int i = 0;
        for (Alert info : infos.values())
        {
            infoStrings[i++] = info.message;
        }
        infoPublishers.set(infoStrings);
        String[] warningStrings = new String[warnings.size()];
        i = 0;
        for (Alert warning : warnings.values())
        {
            warningStrings[i++] = warning.message;
        }
        warningPublishers.set(warningStrings);
        String[] errorStrings = new String[errors.size()];
        i = 0;
        for (Alert error : errors.values())
        {
            errorStrings[i++] = error.message;
        }
        errorPublishers.set(errorStrings);
    }
}
