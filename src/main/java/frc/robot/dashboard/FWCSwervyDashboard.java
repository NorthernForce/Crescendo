package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.dashboard.sendables.Camera;
import frc.robot.utils.AutonomousRoutine;

/**
 * This is a Dashboard specifically meant to interface with FWC for the swervy robot
 */
public class FWCSwervyDashboard extends FWCDashboard implements Dashboard
{
    protected final Field2d field;
    protected final Field2d autoField;
    protected final Camera camera;
    /**
     * Creates a new SwervyDashboard
     */
    public FWCSwervyDashboard()
    {
        super("FWC");
        field = new Field2d();
        autoField = new Field2d();
        camera = new Camera();
        addSendable("auto_field", autoField);
        addSendable("Camera", camera);
        addSendable("Field", field);
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
    }
    public void setCurrentAutonomousRoutine(AutonomousRoutine routine)
    {
        autoField.setRobotPose(routine.startingPose().get());
    }
}
