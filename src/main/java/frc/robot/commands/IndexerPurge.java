package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class IndexerPurge extends Command
{
    protected final Indexer indexer;
    protected final double speed;
    public IndexerPurge(Indexer indexer, double speed)
    {
        this.indexer = indexer;
        this.speed = speed;
        addRequirements(indexer);
    }
    @Override
    public void initialize()
    {
        indexer.startMotor(speed);
    }
    @Override
    public void end(boolean isFinished)
    {
        indexer.stopMotor();
    }
}
