package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class IndexerShoot extends Command {
    Indexer indexer;
    double speed;
    public IndexerShoot(Indexer indexer, double speed){
        this.indexer = indexer;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        indexer.startMotor(speed);
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
