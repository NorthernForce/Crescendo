package frc.robot.robots;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.motors.NFRTalonFX;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.constants.CrabbyConstants;
import frc.robot.gyros.NFRPigeon2;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap {
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL2("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Right", 4, 8, 12, true, "drive"),
    };

    public final NFRGyro gyro = new NFRPigeon2(1);
    public final NFRSparkMax intakeMotor = new NFRSparkMax(MotorType.kBrushless, 14);
    public final NFRBeamBreak indexerBeamBreak = new NFRBeamBreak(7);
    public final NFRSparkMax indexerMotor = new NFRSparkMax(MotorType.kBrushless, 16);
    {
        indexerMotor.restoreFactoryDefaults();
        indexerMotor.setSmartCurrentLimit(60);
        indexerMotor.setIdleMode(IdleMode.kBrake);
        indexerMotor.burnFlash();
    }
    {
        intakeMotor.restoreFactoryDefaults();
        intakeMotor.setSmartCurrentLimit(60);
        intakeMotor.setIdleMode(IdleMode.kBrake);
        intakeMotor.burnFlash();
    }
    public final NFRTalonFX shooterMotorTop = new NFRTalonFX(CrabbyConstants.ShooterConstants.topShooterConfiguration, 22);
    public final NFRTalonFX shooterMotorBottom = new NFRTalonFX(CrabbyConstants.ShooterConstants.bottomShooterConfiguration, 23);
    public final NFRSparkMax wristSparkMax = new NFRSparkMax(MotorType.kBrushless, 17);
    {
        wristSparkMax.setSmartCurrentLimit(40);
        wristSparkMax.setIdleMode(IdleMode.kBrake);
    }
    public final NFRSparkMax climberMotor = new NFRSparkMax(MotorType.kBrushless, 13);
    {
        climberMotor.restoreFactoryDefaults();
        climberMotor.setSmartCurrentLimit(80);
        climberMotor.setIdleMode(IdleMode.kBrake);
        climberMotor.burnFlash();
    }
}
