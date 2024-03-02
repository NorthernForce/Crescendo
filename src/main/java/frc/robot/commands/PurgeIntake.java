package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Intake;

public class PurgeIntake extends ParallelCommandGroup
{
    public PurgeIntake(Intake intake, double speed)
    {
        addCommands(
            new RunIntake(intake, speed)
        );
    }
}
