package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
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
    /**
     * Creates a new Dashboard
     * @param tablePath the path to the table
     */
    public Dashboard(String tablePath)
    {
        table = NetworkTableInstance.getDefault().getTable(tablePath);
        autoChooser = new SendableChooser<>();
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
    }
    /**
     * Displays autonomous routines
     * @param name the name of the SendableChooser
     * @param routines the routines to choose from
     */
    public void displayAutonomousRoutines(String name, List<AutonomousRoutine> routines)
    {
        for (var routine : routines)
        {
            autoChooser.addOption(routine.name(), routine);
        }
        addSendable(name, autoChooser);
    }
    /**
     * Gets the currently selected autonomous routine
     * @return the selected AutonomousRoutine
     */
    public AutonomousRoutine getSelectedRoutine()
    {
        return autoChooser.getSelected();
    }
}
