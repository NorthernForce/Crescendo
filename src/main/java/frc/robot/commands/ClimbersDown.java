package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class ClimbersDown extends Command{
    private Climber climber;
    private double speed;
    private double currentLimit;
    /**
     * Lowers the climbers (raising the robot)
     * @param climber The climber object
     * @param speed The speed at which the climbing motor will run (MUST BE POSITIVE)
     * @param currentLimit If the current being drawn by the motor is greater than this number it will shut off
     */
    public ClimbersDown(Climber climber, double speed, double currentLimit){
        this.climber = climber;
        this.speed = speed;
        this.currentLimit = currentLimit;
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
        return climber.getMotorCurrent() > currentLimit; //TODO: Find max motor current
    }
}