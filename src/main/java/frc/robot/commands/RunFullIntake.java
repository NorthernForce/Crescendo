package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class RunFullIntake extends ParallelDeadlineGroup{

    public RunFullIntake(Indexer indexer, Intake intake, double intakeSpeed, double indexerSpeed)
    {
        super(
            new IndexerIntake(indexer, indexerSpeed),
            new RunIntake(intake, intakeSpeed)
        );
    }

}
