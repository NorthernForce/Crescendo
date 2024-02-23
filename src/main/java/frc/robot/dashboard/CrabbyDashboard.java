package frc.robot.dashboard;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.AutonomousRoutine;

/**
 * This is a Dashboard specifically meant to interface with FWC for the crabby robot
 */
public class CrabbyDashboard extends Dashboard
{
    protected final Field2d field;
    protected final Field2d autoField;
    /**
     * Creates a new CrabbyDashboard
     */
    public CrabbyDashboard()
    {
        super("FWC");
        field = new Field2d();
        autoField = new Field2d();
        addSendable("field", field);
        addSendable("auto_field", autoField);
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
    /**
     * Displays the command to calibrate the swerve drive on the dashboard
     * @param command the command to calibrate the swerve drive
     */
    public void addCalibrateCommand(Command command)
    {
        addSendable("calibrate_swerve", command);
    }
    /**
     * Displays the command to play music on the dashboard
     * @param command the command to play music
     */
    public void addMusicPlayingCommand(Command command)
    {
        addSendable("play_music", command);
    }
    /**
     * Displays the chooser for the music on the dashboard
     * @param command the chooser for the music
     */
    public void addMusicChooser(SendableChooser<String> chooser)
    {
        addSendable("music_selector", chooser);
    }
}
