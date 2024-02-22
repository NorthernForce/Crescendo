package frc.robot.robots;

import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.utils.SwerveModuleHelpers;

public class SwervyMap
{
    public final NFRSwerveModule[] modules = new NFRSwerveModule[] {
        SwerveModuleHelpers.createMk3Slow("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk3Slow("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk3Slow("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk3Slow("Back Right", 4, 8, 12, true, "drive")
    };
    public final NFRPigeon2 gyro = new NFRPigeon2(13);
    public final NFRSparkMax wristSparkMax = new NFRSparkMax(MotorType.kBrushless, 14, 15);
    {
        wristSparkMax.setFollowerOppose(0);
        wristSparkMax.setIdleMode(IdleMode.kBrake);
    }
}