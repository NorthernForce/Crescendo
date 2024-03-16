package frc.robot.subsystems;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.gyros.NFRGyro;
import org.northernforce.subsystems.drive.NFRSwerveDrive;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.dashboard.CrabbyDashboard;

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
     * Creates a new NFRSwerveDrive.
     * @param config the configuration for the swerve drive.
     * @param modules the array of modules
     * @param offsets the array of offsets... this should be equal in length to that of the modules.
     * @param gyro the imu.
     */
    public SwerveDrive(NFRSwerveDriveConfiguration config, NFRSwerveModule[] modules, Translation2d[] offsets,
    NFRGyro gyro, CrabbyDashboard dashboard) {
        super(config, modules, offsets, gyro);
        dashboard.swerveDisplay.setSuppliersNotDesired(this::getModules, () -> gyro.getGyroYaw().getRadians());
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
        poseEstimator.resetPosition(getRotation(), getPositions(), newPose);
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
    @Override
    public Pose2d getEstimatedPose()
    {
        Pose2d pose = poseEstimator.getEstimatedPosition();
        return new Pose2d(pose.getTranslation(), Rotation2d.fromRadians(MathUtil.angleModulus(pose.getRotation().getRadians())));
    }
    public void scheduleCommands(NFRSwerveModuleSetState[] setStateCommands)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
    }
    public void drive(ChassisSpeeds speeds, NFRSwerveModuleSetState[] setStateCommands, boolean optimize, boolean fieldRelative)
    {
        if (fieldRelative)
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, getRotation());
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = toModuleStates(speeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i], getModules()[i].getRotation()) : states[i]);
        }
    }
    public void cancelCommands(NFRSwerveModuleSetState[] setStateCommands)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.cancel();
        }
    }
    public double getSpeed()
    {
        var chassisSpeeds = getChassisSpeeds();
        return new Translation2d(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond).getDistance(new Translation2d());
    }
}
