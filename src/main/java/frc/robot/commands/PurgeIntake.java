package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class PurgeIntake extends ParallelCommandGroup
{
    public PurgeIntake(Intake intake, Indexer indexer, double intakeSpeed, double indexerSpeed)
    {
        addCommands(
            new RunIntake(intake, intakeSpeed),
            new IndexerPurge(indexer, indexerSpeed)
        );
    }
}
