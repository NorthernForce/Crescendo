// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.dashboard.CrabbyDashboard;

public class Shooter extends SubsystemBase {
    private final NFRTalonFX topMotor;
    private final NFRTalonFX bottomMotor;
    private double topTargetSpeed, bottomTargetSpeed;
    private final VelocityVoltage topControl = new VelocityVoltage(0);
    private final VelocityVoltage bottomControl = new VelocityVoltage(0);
    private final StatusSignal<Double> topCurrent, bottomCurrent, topVoltage, bottomVoltage, topAcceleration, bottomAcceleration;
    protected final String name;

    /** Creates a new Shooter. 
     * @param dashboard */
    public Shooter(NFRTalonFX topMotor, NFRTalonFX bottomMotor, CrabbyDashboard dashboard) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;

        dashboard.topShooter.setSupplier(this::getTopMotorVelocity);
        dashboard.bottomShooter.setSupplier(this::getBottomMotorVelocity);
        topCurrent = topMotor.getStatorCurrent();
        bottomCurrent = bottomMotor.getStatorCurrent();
        topAcceleration = topMotor.getAcceleration();
        bottomAcceleration = bottomMotor.getAcceleration();
        topVoltage = topMotor.getMotorVoltage();
        bottomVoltage = bottomMotor.getMotorVoltage();
        name = getName();
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topMotor.setControl(topControl.withVelocity(speed).withSlot(0));
        topTargetSpeed = bottomTargetSpeed = speed;
        Logger.recordOutput(name + "/TopTargetVelocity", topTargetSpeed);
        Logger.recordOutput(name + "/BottomTargetVelocity", bottomTargetSpeed);
        bottomMotor.setControl(bottomControl.withVelocity(speed).withSlot(0));
    }

    /**
     * runs the top motor at the given velocity (in rotations per 100 ms)
     */
    public void runTop(double speed) {
        if (DriverStation.isAutonomousEnabled())
        {
            topTargetSpeed = Math.min(61, speed);
        }
        else
        {
            topTargetSpeed = speed;
        }
        Logger.recordOutput(name + "/TopTargetVelocity", topTargetSpeed);
        topMotor.setControl(topControl.withVelocity(topTargetSpeed).withSlot(0));
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per 100 ms)
     */
    public void runBottom(double speed) {
        if (DriverStation.isAutonomousEnabled())
        {
            bottomTargetSpeed = Math.min(61, speed);
        }
        else
        {
            bottomTargetSpeed = speed;
        }
        Logger.recordOutput(name + "/BottomTargetVelocity", bottomTargetSpeed);
        bottomMotor.setControl(bottomControl.withVelocity(bottomTargetSpeed).withSlot(0));
    }

    public double getTopMotorVelocity() {
        return topMotor.getSelectedEncoder().getVelocity();
    }

    public double getBottomMotorVelocity() {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    @AutoLogOutput(key = "{name}/TopAcceleration")
    public double getTopMotorAcceleration() {
        return topAcceleration.getValueAsDouble();
    }

    @AutoLogOutput(key = "{name}/BottomAcceleration")
    public double getBottomMotorAcceleration() {
        return bottomAcceleration.getValueAsDouble();
    }

    @AutoLogOutput(key = "{name}/TopCurrent")
    public double getTopMotorCurrent() {
        return topCurrent.getValueAsDouble();
    }

    @AutoLogOutput(key = "{name}/BottomCurrent")
    public double getBottomMotorCurrent() {
        return bottomCurrent.getValueAsDouble();
    }

    @AutoLogOutput(key = "{name}/TopVoltage")
    public double getTopMotorVoltage() {
        return topVoltage.getValueAsDouble();
    }

    @AutoLogOutput(key = "{name}/BottomVoltage")
    public double getBottomMotorVoltage() {
        return bottomVoltage.getValueAsDouble();
    }

    public boolean isAtSpeed(double tolerance)
    {
        return Math.abs(getTopMotorVelocity() - topTargetSpeed) < tolerance
            && Math.abs(getBottomMotorVelocity() - bottomTargetSpeed) < tolerance;
    }
    @AutoLogOutput(key="{name}/IsRunning")
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
    }
}
