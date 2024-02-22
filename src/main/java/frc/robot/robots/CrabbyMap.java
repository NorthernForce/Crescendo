package frc.robot.robots;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.Intake;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap {
    public final NFRSwerveModule[] modules = new NFRSwerveModule[] {
        SwerveModuleHelpers.createMk4iL3("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Right", 4, 8, 12, true, "drive")
    };
    public final NFRGyro gyro = new NFRPigeon2(13);
    public final Intake intake = new Intake(17, 18, 19, 20); // TODO: set actual intake motors
    public final NFRSparkMax indexerMotor = new NFRSparkMax(MotorType.kBrushless, 16);
    public final NFRBeamBreak indexerBeamBreak = new NFRBeamBreak(0);
}
