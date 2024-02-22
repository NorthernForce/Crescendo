package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

public class Shoot extends SequentialCommandGroup
{
    public Shoot(Shooter shooter, Indexer indexer, DoubleSupplier speed, double tolerance, double indexerSpeed)
    {
        addCommands(
            new RampShooter(shooter, speed, tolerance),
            new IndexerShoot(indexer, indexerSpeed),
            new WaitCommand(0.2),
            new RestShooter(shooter)
        );
    }
}
