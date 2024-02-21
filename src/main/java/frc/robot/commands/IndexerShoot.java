package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class IndexerShoot extends Command {
    Indexer indexer;
    public IndexerShoot(Indexer indexer){
        this.indexer = indexer;
    }

    @Override
    public void initialize() {
        indexer.startMotor();
    }

    @Override
    public void end(boolean interrupted) {
        indexer.stopMotor();
    }

    @Override
    public boolean isFinished() {
        return indexer.getBeamBreak().beamIntact();
    }
}
