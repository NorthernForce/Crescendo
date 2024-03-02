// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private final NFRMotorController topMotor;
    private final NFRMotorController bottomMotor; 

    /** Creates a new Shooter. */
    public Shooter(NFRMotorController topMotor, NFRMotorController bottomMotor) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topMotor.setVelocity(0, speed);
        bottomMotor.setVelocity(0, speed);
    }

    /**
     * runs the top motor at the given velocity (in rotations per 100 ms)
     */
    public void runTop(double speed) {
        topMotor.setVelocity(0, speed);
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per 100 ms)
     */
    public void runBottom(double speed) {
        bottomMotor.setVelocity(0, speed);
    }

    public double getTopMotorVelocity() {
        return topMotor.getSelectedEncoder().getVelocity();
    }

    public double getBottomMotorVelocity() {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    public void stop() {
        topMotor.set(0);
        bottomMotor.set(0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
