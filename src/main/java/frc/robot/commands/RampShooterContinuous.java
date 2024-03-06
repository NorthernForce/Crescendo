// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RampShooterContinuous extends Command {
    private DoubleSupplier speed, topspin;
    private Shooter shooter;
    /** Creates a new RunShooter. 
     * @param speed the velocity to run the motor at (in rotation per 100ms)
    */
    public RampShooterContinuous(Shooter shooter, DoubleSupplier speed) {
    // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooter);
        this.speed = speed;
        this.shooter = shooter;
        this.topspin = () -> 0;
    }
    public RampShooterContinuous(Shooter shooter, DoubleSupplier speed, DoubleSupplier topspin) {
    // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooter);
        this.speed = speed;
        this.shooter = shooter;
        this.topspin = topspin;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        shooter.run(speed.getAsDouble(), topspin.getAsDouble()); //rotations per second
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
