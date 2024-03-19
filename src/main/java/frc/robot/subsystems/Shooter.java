// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.utils.LoggableHardware;

public class Shooter extends SubsystemBase implements LoggableHardware {
    private final NFRMotorController topMotor;
    private final NFRMotorController bottomMotor;
    private double topTargetSpeed, bottomTargetSpeed;
    private final double tolerance;

    /** Creates a new Shooter. 
     * @param dashboard */
    public Shooter(NFRMotorController topMotor, NFRMotorController bottomMotor, double tolerance, CrabbyDashboard dashboard) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;
        this.tolerance = tolerance;

        dashboard.topShooter.setSupplier(this::getTopMotorVelocity);
        dashboard.bottomShooter.setSupplier(this::getBottomMotorVelocity);
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topMotor.setVelocity(0, speed);
        topTargetSpeed = bottomTargetSpeed = speed;
        bottomMotor.setVelocity(0, speed);
    }

    /**
     * runs the top motor at the given velocity (in rotations per 100 ms)
     */
    public void runTop(double speed) {
        topTargetSpeed = speed;
        topMotor.setVelocity(0, speed);
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per 100 ms)
     */
    public void runBottom(double speed) {
        bottomTargetSpeed = speed;
        bottomMotor.setVelocity(0, speed);
    }

    public double getTopMotorVelocity() {
        return topMotor.getSelectedEncoder().getVelocity();
    }

    public double getBottomMotorVelocity() {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    public boolean isAtSpeed()
    {
        return Math.abs(getTopMotorVelocity() - topTargetSpeed) < tolerance
            && Math.abs(getBottomMotorVelocity() - bottomTargetSpeed) < tolerance;
    }

    public boolean isRunning()
    {
        return topTargetSpeed != 0 || bottomTargetSpeed != 0;
    }

    public void stop() {
        topMotor.set(0);
        bottomMotor.set(0);
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void startLogging(double period) {
    }

    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/TopTargetSpeed", topTargetSpeed);
        Logger.recordOutput(name + "/BottomTargetSpeed", bottomTargetSpeed);
        Logger.recordOutput(name + "/TopSpeed", getTopMotorVelocity());
        Logger.recordOutput(name + "/BottomSpeed", getBottomMotorVelocity());
        Logger.recordOutput(name + "/IsRunning", isRunning());
        Logger.recordOutput(name + "/IsAtSpeed", isAtSpeed());
    }
}
