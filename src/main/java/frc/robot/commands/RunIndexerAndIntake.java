package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class RunIndexerAndIntake extends ParallelDeadlineGroup {
    public RunIndexerAndIntake(Indexer indexer, Intake intake, double indexerSpeed, double intakeSpeed)
    {
        super(new RunIndexer(indexer, indexerSpeed), new RunIntake(intake, intakeSpeed));
    }
}
