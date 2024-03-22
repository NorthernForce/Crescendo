package org.northernforce.subsystems.drive;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

/**
 * The subsystem used for a swerve drive.
 */
public class NFRSwerveDrive extends NFRDrive
{
    /**
     * The configuration of the swerve drive.
     */
    public static class NFRSwerveDriveConfiguration extends NFRDriveConfiguration
    {
        /**
         * Creates a new configuration.
         * @param name the name of the swerve drive. Typically "drive" or something similar.
         */
        public NFRSwerveDriveConfiguration(String name)
        {
            super(name);
        }
    }
    protected final NFRSwerveDriveConfiguration config;
    protected final NFRSwerveModule[] modules;
    protected final Translation2d[] offsets;
    protected final NFRGyro gyro;
    protected final SwerveDriveKinematics kinematics;
    protected final SwerveDrivePoseEstimator poseEstimator;
    protected Rotation2d gyroOffset;
    protected final SwerveDriveOdometry odometry;
    protected final Notifier notifier;
    
    /**
     * Creates a new NFRSwerveDrive.
     * @param config the configuration for the swerve drive.
     * @param modules the array of modules
     * @param offsets the array of offsets... this should be equal in length to that of the modules.
     * @param gyro the imu.
     */
    public NFRSwerveDrive(NFRSwerveDriveConfiguration config, NFRSwerveModule[] modules, Translation2d[] offsets,
        NFRGyro gyro)
    {
        super(config);
        this.config = config;
        this.modules = modules;
        this.offsets = offsets;
        this.gyro = gyro;
        kinematics = new SwerveDriveKinematics(offsets);
        poseEstimator = new SwerveDrivePoseEstimator(kinematics, gyro.getGyroYaw(), getPositions(), new Pose2d());
        gyroOffset = new Rotation2d();
        odometry = new SwerveDriveOdometry(kinematics, gyro.getGyroYaw(), getPositions());
        notifier = new Notifier(this::updateOdometry);
        notifier.startPeriodic(0.02);
    }
    /**
     * Returns an array of all of the positions of the swerve modules
     * @return an array of SwerveModulePositions
     */
    public SwerveModulePosition[] getPositions()
    {
        SwerveModulePosition[] positions = new SwerveModulePosition[modules.length];
        for (int i = 0; i < modules.length; i++)
        {
            positions[i] = modules[i].getPosition();
        }
        return positions;
    }
    /**
     * Gets the estimated pose that is a combination of odometry and vision estimates
     * @return pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public Pose2d getEstimatedPose()
    {
        return poseEstimator.getEstimatedPosition();
    }
    /**
     * Resets the pose to a new pose. This is meant for at the start of a match. Not meant for vision readings.
     * @param newPose pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public void resetPose(Pose2d newPose)
    {
        if (DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red)
        {
            gyroOffset = gyro.getGyroYaw().unaryMinus().plus(newPose.getRotation().plus(Rotation2d.fromDegrees(180)));
        }
        else
        {
            gyroOffset = gyro.getGyroYaw().unaryMinus().plus(newPose.getRotation());
        }
        poseEstimator.resetPosition(gyro.getGyroYaw(), getPositions(), newPose);
    }
    /**
     * Adds a vision estimate that is factored into the pose estimation.
     * @param timestamp timestamp of the pose. Seconds.
     * @param pose pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public void addVisionEstimate(double timestamp, Pose2d pose)
    {
        poseEstimator.addVisionMeasurement(pose, timestamp);
    }
    /**
     * Returns an array of all of the states of the swerve modules
     * @return an array of SwerveModuleStates
     */
    public SwerveModuleState[] getStates()
    {
        SwerveModuleState[] states = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++)
        {
            states[i] = modules[i].getState();
        }
        return states;
    }
    /**
     * Gets the current chassis speeds as recored by the encoder
     * @return chassis speeds which holds vx, vy, and vtheta.
     */
    @Override
    public ChassisSpeeds getChassisSpeeds()
    {
        return kinematics.toChassisSpeeds(getStates());
    }
    /**
     * Gets the swerve module instances
     * @return an array of the swerve modules
     */
    public NFRSwerveModule[] getModules()
    {
        return modules;
    }
    /**
     * Converts chassis speeds to target swerve module states
     * @param speeds the vector/twist of target chassis speed.
     * @return an array of swerve module states.
     */
    public SwerveModuleState[] toModuleStates(ChassisSpeeds speeds)
    {
        return kinematics.toSwerveModuleStates(speeds);
    }
    /**
     * Updates the pose estimations with the latest encoder readings.
     */
    @Override
    public void periodic()
    {
    }
    /**
     * Updates the odometry and pose estimator with module positions.
     */
    public void updateOdometry()
    {
        poseEstimator.updateWithTime(System.currentTimeMillis() / 1000.0, gyro.getGyroYaw(), getPositions());
        odometry.update(gyro.getGyroYaw(), getPositions());
    }

    /**
     * Gets the odometry
     * @return the odometry
     */
    public SwerveDriveOdometry getOdometry()
    {
        return odometry;
    }
    /**
     * Gets the rotation of the swerve module as reported by the gyroscope.
     * @return
     */
    public Rotation2d getRotation()
    {
        return gyro.getGyroYaw().plus(gyroOffset);
    }
    /**
     * Clears rotation reported by the gyroscope by changing the offset. 
     */
    public void clearRotation()
    {
        gyroOffset = gyro.getGyroYaw().unaryMinus();
    }
    /**
     * Gets the pose estimated by the odometry, not affected by vision measurements.
     * @return odometry.getPoseMeters()
     */
    public Pose2d getOdometryPose()
    {
        return odometry.getPoseMeters();
    }
    /**
     * Gets the state of the swerve drive in which all of the motors are pointing inwards.
     * @return the stop state
     */
    public SwerveModuleState[] getStopState()
    {
        SwerveModuleState[] states = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++)
        {
            states[i] = new SwerveModuleState();
            states[i].speedMetersPerSecond = 0;
            states[i].angle = Rotation2d.fromRadians(Math.atan2(offsets[i].getX(), offsets[i].getY()));
        }
        return states;
    }
}
