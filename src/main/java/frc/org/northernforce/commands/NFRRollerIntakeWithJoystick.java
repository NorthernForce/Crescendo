// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.northernforce.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.arm.NFRRollerIntake;

import edu.wpi.first.wpilibj2.command.Command;

/** Runs a roller intake by boolean supplier*/
public class NFRRollerIntakeWithJoystick extends Command {
    protected final NFRRollerIntake intake;
    protected final DoubleSupplier speedSupplier;
    protected final boolean stopOnCommandEnd;

    /**
     * Sets speed of roller intake (command will not stop motor)
     * @param intake The roller intake to set speed of 
     * @param speed The speed to set
    */
    public NFRRollerIntakeWithJoystick(NFRRollerIntake intake, DoubleSupplier speedSupplier) {
        addRequirements(intake);
        this.intake = intake;
        this.speedSupplier = speedSupplier;
        this.stopOnCommandEnd = false;
    }

    /**
     * Sets speed of roller intake
     * @param intake The roller intake to set speed of 
     * @param speed The speed to set
     * @param stopOnCommandEnd If true motor will stop on command end
    */
    public NFRRollerIntakeWithJoystick(NFRRollerIntake intake, DoubleSupplier speedSupplier, boolean stopOnCommandEnd) {
        addRequirements(intake);
        this.intake = intake;
        this.speedSupplier = speedSupplier;
        this.stopOnCommandEnd = stopOnCommandEnd;
    }

    @Override
    public void execute() {
        intake.setSpeed(speedSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        if(stopOnCommandEnd) {
            intake.setSpeed(0);
        }
    }
}
