package frc.robot;

import java.util.Map;

import org.northernforce.util.NFRRobotChooser;
import org.northernforce.util.NFRRobotContainer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.robots.SwervyContainer;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private NFRRobotContainer container;
  private SendableChooser<Pose2d> poseChooser;
  private SendableChooser<Command> autonomousChooser;
  private Command autonomousCommand;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    container = new NFRRobotChooser(() -> new SwervyContainer(), Map.of("Swervy", () -> new SwervyContainer())).getNFRRobotContainer();
    poseChooser = new SendableChooser<>();
    autonomousChooser = new SendableChooser<>();
    for (var pair : container.getStartingLocations().entrySet())
    {
      poseChooser.addOption(pair.getKey(), pair.getValue());
    }
    for (var pair : container.getAutonomousOptions().entrySet())
    {
      autonomousChooser.addOption(pair.getKey(), pair.getValue());
    }
    var pair = container.getDefaultAutonomous();
    autonomousChooser.setDefaultOption(pair.getFirst(), pair.getSecond());
    Shuffleboard.getTab("Autonomous").add("Starting location?", poseChooser);
    Shuffleboard.getTab("Autonomous").add("Autonomous routine?", autonomousChooser);
  }
  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    container.periodic();
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    autonomousCommand = autonomousChooser.getSelected();
    var pose = poseChooser.getSelected();
    if (pose != null)
    {
      container.setInitialPose(pose);
    }
    if (autonomousCommand != null)
    {
      autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    container.autonomousPeriodic();
  }

  @Override
  public void teleopInit() {
    if (autonomousCommand != null)
    {
      autonomousCommand.cancel();
    }
    CommandScheduler.getInstance().getActiveButtonLoop().clear();
    GenericHID driverController;
    if (DriverStation.getJoystickIsXbox(0))
    {
      driverController = new XboxController(0);
    }
    else
    {
      driverController = new GenericHID(0);
    }
    XboxController manipulatorController = new XboxController(1);
    container.bindOI(driverController, manipulatorController);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}