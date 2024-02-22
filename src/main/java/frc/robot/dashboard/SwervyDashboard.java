package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.utils.AutonomousRoutine;

/**
 * This is a Dashboard specifically meant to interface with FWC for the swervy robot
 */
public class SwervyDashboard extends Dashboard
{
    protected final Field2d field;
    protected final Field2d autoField;
    /**
     * Creates a new SwervyDashboard
     */
    public SwervyDashboard()
    {
        super("FWC");
        field = new Field2d();
        autoField = new Field2d();
        addSendable("field", field);
        addSendable("auto_field", autoField);
    }
    /**
     * Updates the robot pose
     * @param pose the new pose
     */
    public void updateRobotPose(Pose2d pose)
    {
        field.setRobotPose(pose);
    }
    @Override
    public void displayAutonomousRoutines(List<AutonomousRoutine> routines)
    {
        displayAutonomousRoutines("autonomous", routines);
        autoChooser.onChange(routine -> {
            if (routine != null)
            {
                setCurrentAutonomousRoutine(routine);
            }
        });
        if (autoChooser.getSelected() != null)
        {
            
            setCurrentAutonomousRoutine(autoChooser.getSelected());
        }
        displayAutonomousRoutines("autonomous", routines);
    }
    public void setCurrentAutonomousRoutine(AutonomousRoutine routine)
    {
        autoField.setRobotPose(routine.startingPose().get());
    }
}
