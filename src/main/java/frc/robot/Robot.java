package frc.robot;

import java.util.Map;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.northernforce.util.NFRRobotChooser;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.robots.CrabbyContainer;
import frc.robot.dashboard.Dashboard;
import frc.robot.robots.SwervyContainer;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.RobotContainer;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {
  private RobotContainer container;
  private Dashboard dashboard;
  private AutonomousRoutine routine;
  private static boolean shouldReplay = false;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    Logger.recordMetadata("ProjectName", "Crescendo"); // Set a metadata value

    if (isReal() || !shouldReplay) {
        Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
        Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
    } else {
        setUseTiming(false); // Run as fast as possible
        String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
        Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
        Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
    }


    container = (RobotContainer)new NFRRobotChooser(() -> new CrabbyContainer(), Map.of(
      "Crabby", () -> new CrabbyContainer(),
      "Swervy", () -> new SwervyContainer())).getNFRRobotContainer();
    dashboard = container.getDashboard();
    dashboard.displayAutonomousRoutines(container.getAutonomousRoutines());
    // Logger.disableDeterministicTimestamps() // See "Deterministic Timestamps" in the "Understanding Data Flow" page
    Logger.start();
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
    routine = dashboard.getSelectedRoutine();
    if (routine != null)
    {
      container.setInitialPose(routine.startingPose().get());
      routine.command().schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    container.autonomousPeriodic();
  }

  @Override
  public void teleopInit() {
    if (routine != null)
    {
      routine.command().cancel();
    }
    CommandScheduler.getInstance().getActiveButtonLoop().clear();
    container.bindOI();
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