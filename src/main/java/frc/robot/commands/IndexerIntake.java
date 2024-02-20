package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class IndexerIntake extends Command {
    Indexer indexer;
    public IndexerIntake(Indexer indexer){
        this.indexer = indexer;
    }

    @Override
    public void initialize() {
        indexer.startMotor();
        System.out.println("IndexerIntake Command initialized");
    }

    @Override
    public void end(boolean interrupted) {
        indexer.stopMotor();
        System.out.println("IndexerIntake Command ended");
    }

    @Override
    public boolean isFinished() {
        return indexer.getBeamBreak().beamBroken();
    }
}
