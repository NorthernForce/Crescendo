package frc.robot;

import java.util.Map;

import org.northernforce.util.NFRRobotChooser;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
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
public class Robot extends TimedRobot {
  private RobotContainer container;
  private Dashboard dashboard;
  private AutonomousRoutine routine;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    container = (RobotContainer)new NFRRobotChooser(() -> new CrabbyContainer(), Map.of(
      "Crabby", () -> new CrabbyContainer(),
      "Swervy", () -> new SwervyContainer())).getNFRRobotContainer();
    dashboard = container.getDashboard();
    dashboard.displayAutonomousRoutines(container.getAutonomousRoutines());
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