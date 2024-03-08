package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.dashboard.sendables.Camera;
import frc.robot.dashboard.sendables.StatusLightManager;
import frc.robot.utils.AutonomousRoutine;

/**
 * This is a Dashboard specifically meant to interface with FWC for the crabby robot
 */
public class CrabbyDashboard extends Dashboard
{
    protected final Field2d field;
    protected final Field2d autoField;
    protected final Camera camera;
    protected final StatusLightManager statusLightManager;
    /**
     * Creates a new CrabbyDashboard
     */
    public CrabbyDashboard()
    {
        super("FWC");
        field = new Field2d();
        autoField = new Field2d();
        camera = new Camera();
        statusLightManager = new StatusLightManager(this); //adds all status lights
        addSendable("Field", field);
        addSendable("auto_field", autoField);
        addSendable("Camera", camera);
    }
    /**
     * Updates the robot pose to be put on the dashboard
     * @param pose the new pose
     */
    public void updateRobotPose(Pose2d pose)
    {
        field.setRobotPose(pose);
    }
    @Override
    public void displayAutonomousRoutines(List<AutonomousRoutine> routines)
    {
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
