package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class ClimbersUp extends Command{
    private Climber climber;
    private double speed;
    /**
     * Raises the climbers (lowering the robot)
     * @param climber The climber object
     * @param speed The speed at which the climbing motor will run (MUST BE NEGATIVE)
     */
    public ClimbersUp(Climber climber, double speed){
        this.climber = climber;
        addRequirements(climber);
        this.speed = speed;
    }

    @Override
    public void initialize(){
        climber.startMotor(speed);
    }

    @Override
    public void end(boolean interrupted){
        climber.stopMotor();
    }

    @Override
    public boolean isFinished(){
        return climber.isAtTopSoftLimit();
    }
}

