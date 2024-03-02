package frc.robot.robots;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap {
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL3("Front Left", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk4iL3("Front Right", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Left", 4, 8, 12, true, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Right", 3, 7, 11, false, "drive"),
    };

    public final NFRGyro gyro = new NFRPigeon2(1);
    public final NFRSparkMax intakeMotor = new NFRSparkMax(MotorType.kBrushless, 17, 18);

    {
        intakeMotor.setFollowerOppose(0);
    }
    public final NFRSparkMax indexerMotor = new NFRSparkMax(MotorType.kBrushless, 16);
    public final NFRBeamBreak indexerBeamBreak = new NFRBeamBreak(0);
}
