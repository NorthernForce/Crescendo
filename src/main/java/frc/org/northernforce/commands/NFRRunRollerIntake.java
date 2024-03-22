// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.northernforce.commands;

import org.northernforce.subsystems.arm.NFRRollerIntake;

import edu.wpi.first.wpilibj2.command.Command;

/** Runs a roller intake */
public class NFRRunRollerIntake extends Command {
    protected final NFRRollerIntake intake;
    protected final double speed;
    protected final boolean stopOnCommandEnd;

    /**
     * Sets speed of roller intake (command will not stop motor)
     * @param intake The roller intake to set speed of 
     * @param speed The speed to set
    */
    public NFRRunRollerIntake(NFRRollerIntake intake, double speed) {
        addRequirements(intake);
        this.intake = intake;
        this.speed = speed;
        this.stopOnCommandEnd = false;
    }

    /**
     * Sets speed of roller intake
     * @param intake The roller intake to set speed of 
     * @param speed The speed to set
     * @param stopOnCommandEnd If true motor will stop on command end
    */
    public NFRRunRollerIntake(NFRRollerIntake intake, double speed, boolean stopOnCommandEnd) {
        addRequirements(intake);
        this.intake = intake;
        this.speed = speed;
        this.stopOnCommandEnd = stopOnCommandEnd;
    }

    @Override
    public void initialize() {
        intake.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        if(stopOnCommandEnd) {
            intake.setSpeed(0);
        }
    }
    
}
