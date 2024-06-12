package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.BeachBallExtending;

public class ExtendToBeachball extends Command{
    private BeachBallExtending motor;
    public ExtendToBeachball(BeachBallExtending motor) {
        this.motor = motor;
    }

    public void initialize() {
        motor.run();
    }
}
