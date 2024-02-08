package frc.robot.robots;

import java.util.List;
import java.util.Map;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.OrangePi;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.OrangePi.PoseSupplier;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;

public class CrabbyContainer implements RobotContainer
{
  
    protected final OrangePi orangePi;
    protected final TargetCamera aprilTagCamera, noteDetectorCamera;
    protected final PoseSupplier aprilTagSupplier;
    public CrabbyContainer()
    {
        orangePi = new OrangePi(new OrangePiConfiguration("orange pi", "xavier"));
        Shuffleboard.getTab("General").addBoolean("Xavier Connected", orangePi::isConnected);
        noteDetectorCamera = orangePi.new TargetCamera("usb_cam2");
        aprilTagCamera = orangePi.new TargetCamera("usb_cam1");
        aprilTagSupplier = orangePi.new PoseSupplier("usb_cam1", estimate -> {});
    }
    @Override
    public void bindOI(GenericHID driverHID, GenericHID manipulatorHID)
    {
        if (driverHID instanceof XboxController)
        {
        }
        else
        {
        }
    }
    @Override
    public Map<String, Command> getAutonomousOptions()
    {
        return Map.of();
    }
    @Override
    public Map<String, Pose2d> getStartingLocations()
    {
        return Map.of("Simple Starting Location", new Pose2d(5, 5, Rotation2d.fromDegrees(0)));
    }
    @Override
    public Pair<String, Command> getDefaultAutonomous()
    {
        return Pair.of("Do Nothing", Commands.none());
    }
    @Override
    public void setInitialPose(Pose2d pose)
    {
    }
    @Override
    public void periodic()
    {
    }
    @Override
    public List<AutonomousRoutine> getAutonomousRoutines() {
        return List.of(new AutonomousRoutine("Do nothing", new Pose2d(), Commands.none()));
    }
}
