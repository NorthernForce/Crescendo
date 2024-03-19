// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RampShooter extends Command {
    private double speed;
    private Shooter shooter;
    /** Creates a new RampShooter. 
     * @param speed the velocity to run the motor at (in rotation per 100ms)
    */
    public RampShooter(Shooter shooter, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooter);
        this.speed = speed;
        this.shooter = shooter;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        shooter.run(speed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {; //rotations per 100ms
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return shooter.isRunning() && shooter.isAtSpeed();
    }
}
