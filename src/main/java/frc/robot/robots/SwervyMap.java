package frc.robot.robots;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.SwerveModuleHelpers;

public class SwervyMap
{
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk3Slow("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk3Slow("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk3Slow("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk3Slow("Back Right", 4, 8, 12, true, "drive")
    };
    public final NFRPigeon2 gyro = new NFRPigeon2(13);
}