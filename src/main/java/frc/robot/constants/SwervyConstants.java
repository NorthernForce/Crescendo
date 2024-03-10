package frc.robot.constants;
import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.Xavier.XavierConfiguration;

public class SwervyConstants
{
    public static class DriveConstants
    {
        /** X/Y offsets of each swerve module front left -> back right */
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.581025, 0.581025),
            new Translation2d(0.581025, -0.581025),
            new Translation2d(-0.581025, 0.581025),
            new Translation2d(-0.581025, -0.581025)
        };
        public static final NFRSwerveDriveConfiguration config = new NFRSwerveDriveConfiguration("drive");
        public static final PPHolonomicDriveController holonomicDriveController = new PPHolonomicDriveController(
            new PIDConstants(10, 0, 0), // X/Y pid constants
            new PIDConstants(5, 0, 0), // Rotational pid constants
            6, // Max Module Speed
            offsets[0].getDistance(new Translation2d())); // Distance from center
        public static final HolonomicPathFollowerConfig holonomicConfig = new HolonomicPathFollowerConfig(
            new PIDConstants(5),
            new PIDConstants(5),
            6, offsets[0].getDistance(new Translation2d()), new ReplanningConfig());
    }
    public static class OrangePiConstants
    {
        public static final OrangePiConfiguration config = new OrangePiConfiguration("orangepi", "xavier");
        public static final double cameraHeight = Units.inchesToMeters(17);
        public static final Rotation2d cameraPitch = Rotation2d.fromDegrees(0);
    }
    public static class XavierConstants
    {
        public static final XavierConfiguration config = new XavierConfiguration("xavier", "note_detection");
    }
}
