package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.utils.AutonomousRoutine;

public abstract class Dashboard
{
    protected final NetworkTable table;
    protected final SendableChooser<AutonomousRoutine> autoChooser;
    public Dashboard(String tablePath)
    {
        table = NetworkTableInstance.getDefault().getTable(tablePath);
        autoChooser = new SendableChooser<>();
    }
    public void addSendable(String name, Sendable sendable)
    {
        NetworkTable subTable = table.getSubTable(name);
        SendableBuilderImpl builder = new SendableBuilderImpl();
        builder.setTable(subTable);
        SendableRegistry.publish(sendable, builder);
        builder.startListeners();
        subTable.getEntry(".name").setString(name);
    }
    public void displayAutonomousRoutines(String name, List<AutonomousRoutine> routines)
    {
        for (var routine : routines)
        {
            autoChooser.addOption(routine.name(), routine);
        }
        addSendable(name, autoChooser);
    }
    public AutonomousRoutine getSelected()
    {
        return autoChooser.getSelected();
    }
}
