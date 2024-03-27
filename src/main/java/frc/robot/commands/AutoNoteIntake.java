package frc.robot.commands;

import org.northernforce.commands.NFRSwerveModuleSetState;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.Xavier;

public class AutoNoteIntake extends ParallelRaceGroup
{
    public AutoNoteIntake(Xavier xavier, SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Intake intake, Indexer indexer) {
        addCommands(
            new AutoNoteSeek(drive, setStateCommands, xavier),
            new RunIndexerAndIntake(indexer, intake,
                CrabbyConstants.IndexerConstants.indexerSpeed,
                CrabbyConstants.IntakeConstants.intakeSpeed));
    }
}
