package frc.robot.constants;

import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;

public class SwervyConstants
{
    public static class Drive
    {
        /** X/Y offsets of each swerve module front left -> back right */
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.581025, 0.581025),
            new Translation2d(0.581025, -0.581025),
            new Translation2d(-0.581025, 0.581025),
            new Translation2d(-0.581025, -0.581025)
        };
        public static final NFRSwerveDriveConfiguration config = new NFRSwerveDriveConfiguration("drive");
    }
    public static class OrangePi
    {
        public static final OrangePiConfiguration config = new OrangePiConfiguration("orangepi", "xavier");
    }
}
