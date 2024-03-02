package frc.robot.robots;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap {
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL3("Front Left", 2, 6, 10, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Front Right", 1, 5, 9, true, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Left", 4, 8, 12, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Right", 3, 7, 11, true, "drive"),
    };

    public final NFRGyro gyro = new NFRPigeon2(1);
    public final NFRSparkMax intakeMotor = new NFRSparkMax(MotorType.kBrushless, 18);
    public final NFRBeamBreak intakeBeamBreak = new NFRBeamBreak(0);
    public final NFRSparkMax wristSparkMax = new NFRSparkMax(MotorType.kBrushless, 14);
    {
        wristSparkMax.setIdleMode(IdleMode.kBrake);
    }
}
