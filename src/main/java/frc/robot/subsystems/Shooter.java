// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.dashboard.CrabbyDashboard;

public class Shooter extends SubsystemBase {
    private final NFRTalonFX topMotor;
    private final NFRTalonFX bottomMotor;
    private final VelocityVoltage topControl, bottomControl;
    private final VoltageOut topVoltage, bottomVoltage;
    private double topTargetSpeed, bottomTargetSpeed;
    private final SysIdRoutine topSysID, bottomSysID;
    private final StatusSignal<Double> topAcceleration, bottomAcceleration, topCurrent, bottomCurrent,
        m_topVoltage, m_bottomVoltage;

    /** Creates a new Shooter. 
     * @param dashboard */
    public Shooter(NFRTalonFX topMotor, NFRTalonFX bottomMotor, CrabbyDashboard dashboard) {
        this.topMotor = topMotor;
        this.bottomMotor = bottomMotor;
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;

        dashboard.topShooter.setSupplier(this::getTopMotorVelocity);
        dashboard.bottomShooter.setSupplier(this::getBottomMotorVelocity);

        topControl = new VelocityVoltage(0).withSlot(0);
        bottomControl = new VelocityVoltage(0).withSlot(0);

        topVoltage = new VoltageOut(0);
        bottomVoltage = new VoltageOut(0);

        topAcceleration = topMotor.getAcceleration();
        bottomAcceleration = bottomMotor.getAcceleration();
        topCurrent = topMotor.getStatorCurrent();
        bottomCurrent = bottomMotor.getStatorCurrent();
        m_topVoltage = topMotor.getMotorVoltage();
        m_bottomVoltage = bottomMotor.getMotorVoltage();

        topSysID = new SysIdRoutine(new SysIdRoutine.Config(),
            new SysIdRoutine.Mechanism(volts -> {
                topMotor.setControl(topVoltage.withOutput(volts.in(Units.Volts)));
            }, log -> {
                log.motor("top-motor")
                    .angularPosition(Units.Rotations.of(topMotor.getSelectedEncoder().getPosition()))
                    .angularVelocity(Units.RotationsPerSecond.of(topMotor.getSelectedEncoder().getVelocity()))
                    .angularAcceleration(Units.RotationsPerSecond.per(Units.Second).of(topAcceleration.getValueAsDouble()))
                    .current(Units.Amps.of(topCurrent.getValueAsDouble()))
                    .voltage(Units.Volts.of(m_topVoltage.getValueAsDouble()));
            }, this, "top-motor")
        );

        bottomSysID = new SysIdRoutine(new SysIdRoutine.Config(),
            new SysIdRoutine.Mechanism(volts -> {
                bottomMotor.setControl(bottomVoltage.withOutput(volts.in(Units.Volts)));
            }, log -> {
                log.motor("bottom-motor")
                    .angularPosition(Units.Rotations.of(bottomMotor.getSelectedEncoder().getPosition()))
                    .angularVelocity(Units.RotationsPerSecond.of(bottomMotor.getSelectedEncoder().getVelocity()))
                    .angularAcceleration(Units.RotationsPerSecond.per(Units.Second).of(bottomAcceleration.getValueAsDouble()))
                    .current(Units.Amps.of(bottomCurrent.getValueAsDouble()))
                    .voltage(Units.Volts.of(m_bottomVoltage.getValueAsDouble()));
            }, this, "bottom-motor")
        );
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
     * (-) velocity is outtake (in current design) (i think)
     */
    public void run(double speed) {
        topTargetSpeed = bottomTargetSpeed = speed;
        topMotor.setControl(topControl.withVelocity(speed));
        bottomMotor.setControl(bottomControl.withVelocity(speed));
    }

    /**
     * runs the top motor at the given velocity (in rotations per 100 ms)
     */
    public void runTop(double speed) {
        topTargetSpeed = speed;
        topMotor.setControl(topControl.withVelocity(speed));
    }

    /**
     * runs the bottom motor at the given velocity (in rotations per 100 ms)
     */
    public void runBottom(double speed) {
        bottomTargetSpeed = speed;
        bottomMotor.setControl(bottomControl.withVelocity(speed));
    }

    public double getTopMotorVelocity() {
        return topMotor.getSelectedEncoder().getVelocity();
    }

    public double getBottomMotorVelocity() {
        return bottomMotor.getSelectedEncoder().getVelocity();
    }

    public Command getDynamicTop(SysIdRoutine.Direction direction) {
        return topSysID.dynamic(direction);
    }

    public Command getQuasistaticTop(SysIdRoutine.Direction direction) {
        return topSysID.dynamic(direction);
    }

    public Command getDynamicBottom(SysIdRoutine.Direction direction) {
        return bottomSysID.quasistatic(direction);
    }

    public Command getQuasistaticBottom(SysIdRoutine.Direction direction) {
        return bottomSysID.quasistatic(direction);
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
