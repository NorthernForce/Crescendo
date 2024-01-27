package frc.robot.subsystems;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;

/**
 * Swerve drive that extends NFRSwerve Drive. Used for vital fixes
 */
public class SwerveDrive extends NFRSwerveDrive
{
    /**
     * Creates a new NFRSwerveDrive.
     * @param config the configuration for the swerve drive.
     * @param modules the array of modules
     * @param offsets the array of offsets... this should be equal in length to that of the modules.
     * @param gyro the imu.
     */
    public SwerveDrive(NFRSwerveDriveConfiguration config, NFRSwerveModule[] modules, Translation2d[] offsets,
            NFRGyro gyro) {
        super(config, modules, offsets, gyro);
    }
    /**
     * Updates the odometry and pose estimator with module positions.
     */
    @Override
    public void updateOdometry()
    {
        poseEstimator.updateWithTime(Timer.getFPGATimestamp(), getRotation(), getPositions());
        odometry.update(getRotation(), getPositions());
    }
}
