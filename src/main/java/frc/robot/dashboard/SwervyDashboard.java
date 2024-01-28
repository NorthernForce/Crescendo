package frc.robot.dashboard;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

/**
 * This is a Dashboard specifically meant to interface with FWC for the swervy robot
 */
public class SwervyDashboard extends Dashboard
{
    protected final Field2d field;
    /**
     * Creates a new SwervyDashboard
     */
    public SwervyDashboard()
    {
        super("FWC");
        field = new Field2d();
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
}
