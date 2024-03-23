package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.robots.CrabbyContainer;

public interface CrabbyOI {
    public void bindDriverToXBoxController(CrabbyContainer container, CommandXboxController controller);
    public void bindDriverToJoystick(CrabbyContainer container, CommandGenericHID joystick);
    public void bindManipulatorToXboxController(CrabbyContainer container, CommandXboxController controller);
    public void bindManipulatorToJoystick(CrabbyContainer container, CommandGenericHID joystick);
    public CommandXboxController getDriverController();
    public CommandXboxController getManipulatorController();
}
