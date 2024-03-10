package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.robots.SwervyContainer;

public interface SwervyOI {
    public void bindDriverToXBoxController(SwervyContainer container, CommandXboxController controller);
    public void bindDriverToJoystick(SwervyContainer container, CommandGenericHID joystick);
    public void bindManipulatorToXboxController(SwervyContainer container, CommandXboxController controller);
    public void bindManipulatorToJoystick(SwervyContainer container, CommandGenericHID joystick);
}
