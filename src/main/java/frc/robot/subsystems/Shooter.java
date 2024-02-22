// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.northernforce.motors.NFRMotorController;
import org.northernforce.motors.NFRTalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private final NFRMotorController topMotor;
    private final NFRMotorController bottomMotor; 

    /** Creates a new Shooter. */
    public Shooter(NFRTalonFX topMotor, NFRTalonFX bottomMotor) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per second)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topMotor.setVelocity(0, speed);
        bottomMotor.setVelocity(0, speed);
    }

    /**
     * runs the top motor at the given velocity (in rotations per second)
     */
    public void runTop(double speed) {
        topMotor.setVelocity(0, speed);
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per second)
     */
    public void runBottom(double speed) {
        bottomMotor.setVelocity(0, speed);
    }
    /**
     * Gets the velocity of the top motor in rps
     * @return velocity in rps
     */
    public double getTopVelocity()
    {
        return topMotor.getSelectedEncoder().getVelocity();
    }
    /**
     * Gets the velocity of the bottom motor in rps
     * @return velocity in rps
     */
    public double getBottomVelocity()
    {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
