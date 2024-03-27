package frc.robot.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.WristJoint;

public class MoveClimber extends SequentialCommandGroup {
    public MoveClimber(Climber climber, WristJoint wrist, DoubleSupplier joystickInput) {
        addCommands(
            new NFRRotatingArmJointSetAngle(wrist, CrabbyConstants.WristConstants.climberRotation, CrabbyConstants.WristConstants.tolerance,
                0, true).withInterruptBehavior(InterruptionBehavior.kCancelIncoming), // TODO
            climber.run(() -> climber.startMotor(joystickInput.getAsDouble()))
        );
        addRequirements(climber, wrist);
    }
}
