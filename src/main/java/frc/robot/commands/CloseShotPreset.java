package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class CloseShotPreset extends ParallelCommandGroup {
    public CloseShotPreset(Shooter shooter, WristJoint wrist) {
        addCommands(
            new NFRRotatingArmJointSetAngle(wrist, Rotation2d.fromDegrees(55), Rotation2d.fromDegrees(5),
                0, true).alongWith(Commands.runOnce(() -> wrist.setTargetAngle(CrabbyConstants.WristConstants.closeShotRotation))),
            new RampShooterWithDifferential(shooter, () -> 35, () -> 40)
        );
    }
}
