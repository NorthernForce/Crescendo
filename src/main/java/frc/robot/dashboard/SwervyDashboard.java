package frc.robot.dashboard;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public class SwervyDashboard extends Dashboard
{
    protected final Field2d field;
    public SwervyDashboard()
    {
        super("FWC");
        field = new Field2d();
        addSendable("Field", field);
    }
    public void updateRobotPose(Pose2d pose)
    {
        field.setRobotPose(pose);
    }
}
