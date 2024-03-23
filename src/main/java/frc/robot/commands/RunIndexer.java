package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class RunIndexer extends Command {
    private final Indexer indexer;
    private final double speed;

    /**
     * Creates a new RunIntake command
     * 
     * @param intake the intake subsystem
     * @param speed  raw speed value for intake motors
     */
    public RunIndexer(Indexer indexer, double speed) {
        addRequirements(indexer);
        this.indexer = indexer;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        indexer.run(speed);
    }

    @Override
    public void end(boolean interrupted) {
        indexer.run(0);
    }

    @Override
    public boolean isFinished() {
        return indexer.getBeamBreak().beamBroken();
    }
}
