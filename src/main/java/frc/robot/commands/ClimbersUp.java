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
     * @param currentLimit If the motor starts drawing more current than this number, it will turn off
     */
    public ClimbersUp(Climber climber, double speed, double currentLimit){
        this.climber = climber;
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
        return climber.getBeamBreak().beamBroken();
    }
}

