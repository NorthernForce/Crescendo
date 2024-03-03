// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RampShooterWithDifferential extends Command {
    private DoubleSupplier topSpeed;
    private DoubleSupplier bottomSpeed;
    private Shooter shooter;
    /** Creates a new RunShooter. 
     * @param topSpeed the velocity to run the top motor at (in rotation per 100ms)
     * @param bottomSpeed the velocity to run the bottom motor at (inrotation per 100ms)
    */
    public RampShooterWithDifferential(Shooter shooter, DoubleSupplier topSpeed, DoubleSupplier bottomSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(shooter);
        this.topSpeed = topSpeed;
        this.bottomSpeed = bottomSpeed;
        this.shooter = shooter;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        shooter.runTop(topSpeed.getAsDouble()); //rotations per 100ms
        shooter.runBottom(bottomSpeed.getAsDouble()); //rotations per 100ms
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

