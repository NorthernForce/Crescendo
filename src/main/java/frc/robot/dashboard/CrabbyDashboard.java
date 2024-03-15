package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.dashboard.sendables.Camera;
import frc.robot.dashboard.sendables.SendableGauge;
import frc.robot.dashboard.sendables.SendableNumberSlider;
import frc.robot.dashboard.sendables.SendableSwerve;
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
    protected final SendableSwerve swerveDisplay;
    protected final SendableGauge wristGauge;
    protected final SendableGauge topShooter;
    protected final SendableGauge bottomShooter;
    protected final SendableNumberSlider shooterSlider;
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
        swerveDisplay = new SendableSwerve();
        wristGauge = new SendableGauge(22, 22, 56);
        topShooter = new SendableGauge(0, 0, 45); //TODO Max speeds are made up based on what I remember from initial testing should be tweeked;
        bottomShooter = new SendableGauge(0, 0, 45);
        shooterSlider = new SendableNumberSlider(0, 0, 45);
        addSendable("Field", field);
        addSendable("auto_field", autoField);
        addSendable("Camera", camera);
        addSendable("SwerveDisplay", swerveDisplay);
        addSendable("wristGauge", wristGauge);
        addSendable("TopShooter", topShooter);
        addSendable("BottomShooter", bottomShooter);
        addSendable("ShooterSlider", shooterSlider);
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
