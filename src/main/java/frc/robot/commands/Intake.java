package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class Intake extends Command {
    Indexer indexer;
    public Intake(Indexer indexer){
        this.indexer = indexer;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute(){
        indexer.intake();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
