// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.controls.VelocityVoltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.dashboard.CrabbyDashboard;

public class Shooter extends SubsystemBase {
    private final NFRTalonFX topMotor;
    private final NFRTalonFX bottomMotor;
    private double topTargetSpeed, bottomTargetSpeed;
    private final VelocityVoltage topControl = new VelocityVoltage(0);
    private final VelocityVoltage bottomControl = new VelocityVoltage(0);

    /** Creates a new Shooter. 
     * @param dashboard */
    public Shooter(NFRTalonFX topMotor, NFRTalonFX bottomMotor, CrabbyDashboard dashboard) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;

        dashboard.topShooter.setSupplier(this::getTopMotorVelocity);
        dashboard.bottomShooter.setSupplier(this::getBottomMotorVelocity);
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topMotor.setControl(topControl.withVelocity(speed).withSlot(0));
        topTargetSpeed = bottomTargetSpeed = speed;
        bottomMotor.setControl(bottomControl.withVelocity(speed).withSlot(0));
    }

    /**
     * runs the top motor at the given velocity (in rotations per 100 ms)
     */
    public void runTop(double speed) {
        topTargetSpeed = speed;
        topMotor.setControl(topControl.withVelocity(speed).withSlot(0));
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per 100 ms)
     */
    public void runBottom(double speed) {
        bottomTargetSpeed = speed;
        bottomMotor.setControl(bottomControl.withVelocity(speed).withSlot(0));
    }

    public double getTopMotorVelocity() {
        return topMotor.getSelectedEncoder().getVelocity();
    }

    public double getBottomMotorVelocity() {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    public boolean isAtSpeed(double tolerance)
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
}
