package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class ShootWhenReady extends SequentialCommandGroup {
    public ShootWhenReady(Shooter shooter, Intake intake, double intakeSpeed, double shooterTolerance)
    {
        addCommands(
            new WaitUntilCommand(() -> shooter.isAtSpeed(shooterTolerance)),
            new ShootIntake(intake, intakeSpeed)
        );
    }
}
