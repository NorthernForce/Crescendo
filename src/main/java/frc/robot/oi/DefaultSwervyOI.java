package frc.robot.oi;

import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.FollowNote;
import frc.robot.commands.TurnToTarget;
import frc.robot.robots.SwervyContainer;

public class DefaultSwervyOI implements SwervyOI
{
    @Override
    public void bindDriverToXBoxController(SwervyContainer container, CommandXboxController controller)
    {
        container.drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1), true, true));
        
        controller.b().onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        controller.y().whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));
        
        controller.a().whileTrue(new FollowNote(container.xavier, container.drive, container.setStateCommands,
                () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1), true));
        
        controller.rightBumper().whileTrue(new TurnToTarget(container.drive, container.setStateCommands, new PIDController(1, 0, 0), 
                () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
                () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
                () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1),
                container.aprilTagCamera::getSpeakerTag, true, true));
    }
    @Override
    public void bindDriverToJoystick(SwervyContainer container, CommandGenericHID joystick)
    {
        container.drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(1), 0.1, 1),
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(0), 0.1, 1),
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(4), 0.1, 1), true, true));
        
        joystick.button(2).onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        joystick.button(3).whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));
        
        joystick.button(1).whileTrue(new FollowNote(container.xavier, container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(0), 0.1, 1), true));
    }
    @Override
    public void bindManipulatorToXboxController(SwervyContainer container, CommandXboxController controller)
    {
    }
    @Override
    public void bindManipulatorToJoystick(SwervyContainer container, CommandGenericHID joystick)
    {
    }
}
