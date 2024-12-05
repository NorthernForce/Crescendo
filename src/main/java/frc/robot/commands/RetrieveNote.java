package frc.robot.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.commands.NFRSwerveModuleSetState;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.Xavier;

public class RetrieveNote extends SequentialCommandGroup
{
    public RetrieveNote(
        Xavier xavier,
        SwerveDrive drive,
        NFRSwerveModuleSetState[] setStateCommands,
        Intake intake,
        Indexer indexer,
        DoubleSupplier strafeSupplier,
        boolean useOptimization)
        {
            super(
                new ParallelDeadlineGroup(
                    new RunIndexerAndIntake(indexer, intake, CrabbyConstants.IndexerConstants.indexerSpeed, CrabbyConstants.IntakeConstants.intakeSpeed),
                    new FollowNote(xavier, drive, setStateCommands, strafeSupplier, useOptimization)),
                new PurgeIndexer(indexer, intake, 0.7, -0.7)
                    .withTimeout(0.175)
                    .andThen(new RunIndexerAndIntake(indexer, intake,
                        CrabbyConstants.IndexerConstants.indexerSpeed,
                        CrabbyConstants.IntakeConstants.intakeSpeed)));
    }  
}
