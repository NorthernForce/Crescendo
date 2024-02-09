// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.northernforce.motors.NFRMotorController;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private final int TOP_MOTOR_ID = 0;
    private final int BOTTOM_MOTOR_ID = 0;
    private final NFRMotorController topMotor;
    private final NFRMotorController bottomMotor; 

    /** Creates a new Shooter. */
    public Shooter() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        Slot0Configs slot0Config = new Slot0Configs()
            .withKP(0)
            .withKI(0)
            .withKD(0);

        config.withSlot0(slot0Config);
        // do config here

        topMotor = new NFRTalonFX(config, TOP_MOTOR_ID);
        bottomMotor = new NFRTalonFX(config, BOTTOM_MOTOR_ID);
    }

    /**
     * runs both the top and bottom motors at the given velocity (in rotations per 100 ms)
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

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
