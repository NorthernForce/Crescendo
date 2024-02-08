package frc.robot.subsystems;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.function.Consumer;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.geometry.struct.Pose2dStruct;
import edu.wpi.first.math.geometry.struct.Twist2dStruct;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArraySubscriber;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.networktables.StructSubscriber;
import edu.wpi.first.networktables.NetworkTableEvent.Kind;
import edu.wpi.first.util.struct.Struct;

/**
 * This is a subsystem for the Orange Pi 5+ that runs nfr_ros.
 */
public class OrangePi extends NFRSubsystem
{
    /**
     * This is the configuration class for the OrangePi subsystem.
     */
    public static class OrangePiConfiguration extends NFRSubsystemConfiguration
    {
        protected String tableName;
        /**
         * Create a new OrangePiConfiguration.
         * @param name the name of the subsystem
         */
        public OrangePiConfiguration(String name)
        {
            super(name);
        }
        /**
         * Create a new OrangePiConfiguration.
         * @param name the name of the subsystem
         * @param tableName the name of the table where the orange pi publishes its information
         */
        public OrangePiConfiguration(String name, String tableName)
        {
            super(name);
            this.tableName = tableName;
        }
        /**
         * With table name
         * @param tableName the name of the table where the orange pi publishes its information
         * @return this
         */
        public OrangePiConfiguration withTableName(String tableName)
        {
            this.tableName = tableName;
            return this;
        }
    }
    protected final NetworkTable table;
    protected final StructPublisher<Twist2d> odometryPublisher;
    protected final StructSubscriber<Twist2d> cmdVelSubscriber;
    protected final StructPublisher<Pose2d> globalPosePublisher, targetPosePublisher;
    protected final BooleanPublisher targetPoseCancel;
    protected final StructSubscriber<Pose2d> poseSubscriber;
    /**
     * Create a new orange pi.
     * @param config
     */
    public OrangePi(OrangePiConfiguration config)
    {
        super(config);
        table = NetworkTableInstance.getDefault().getTable(config.tableName);
        odometryPublisher = table.getStructTopic("odometry", new Twist2dStruct()).publish();
        targetPosePublisher = table.getStructTopic("target_pose", new Pose2dStruct()).publish();
        targetPoseCancel = table.getBooleanTopic("target_pose_cancel").publish();
        globalPosePublisher = table.getStructTopic("global_set_pose", new Pose2dStruct()).publish();
        cmdVelSubscriber = table.getStructTopic("cmd_vel", new Twist2dStruct()).subscribe(new Twist2d());
        poseSubscriber = table.getStructTopic("pose", new Pose2dStruct()).subscribe(new Pose2d());
    }
    /**
     * Sets the odometry
     * @param deltaX in m/s
     * @param deltaY in m/s
     * @param deltaTheta change in angle
     * @param stamp in seconds
     */
    public void setOdometry(ChassisSpeeds speeds)
    {
        odometryPublisher.set(new Twist2d(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond));
    }
    /**
     * Sends a target pose
     * @param pose the target pose
     * @param stamp in seconds
     */
    public void sendTargetPose(Pose2d pose, double stamp)
    {
        targetPosePublisher.set(pose);
    }
    /**
     * Cancels a target pose
     */
    public void cancelTargetPose()
    {
        targetPoseCancel.set(true);
    }
    /**
     * Gets the command velocity from nav2
     * @return dx, dy, and dtheta
     */
    public Twist2d getCommandVelocity()
    {
        return cmdVelSubscriber.get();
    }
    // /**
    //  * Sets the map -> base_link pose
    //  * @param pose in meters
    //  * @param stamp in seconds
    //  */
    public Pose2d getPose()
    {
        return poseSubscriber.get();
    }
    /**
     * Checks to see if the orange pi is connected
     * @return whether the orange pi is connected
     */
    public boolean isConnected()
    {
        boolean connectionFound = false;
        for (var connection : NetworkTableInstance.getDefault().getConnections())
        {
            if (connection.remote_id.startsWith("xavier"))
            {
                connectionFound = true;
            }
        }
        return connectionFound;
    }
    public static record TargetDetection(double area, double tx, double ty, double pitch, double yaw, double depth, int fiducialID)
    {
        /**
         * Calculates distance to target
         * @param cameraPitch the pitch of the camera
         * @param cameraHeight the height of the camera in meters
         * @param targetHeight the height of the target in meters
         * @return a distance in meters to the target
         */
        public double calculateDistance(Rotation2d cameraPitch, double cameraHeight, double targetHeight)
        {
            return (targetHeight - cameraHeight) / Math.tan(pitch + cameraPitch.getRadians());
        }
        public double calculateDistanceWithDepth(double cameraHeight, double targetHeight)
        {
            double opposite = cameraHeight - targetHeight;
            return Math.sqrt(depth * depth - opposite * opposite);
        }
    }
    public static class TargetDetectionStruct implements Struct<TargetDetection>
    {
        @Override
        public Class<TargetDetection> getTypeClass()
        {
            return TargetDetection.class;
        }
        @Override
        public String getTypeString()
        {
            return "struct:TargetDetection";
        }
        @Override
        public int getSize()
        {
            return Struct.kSizeDouble * 6 + Struct.kSizeInt64;
        }
        @Override
        public String getSchema()
        {
            return "double area; double tx; double ty; double pitch; double yaw; double depth; long fiducialID";
        }
        @Override
        public TargetDetection unpack(ByteBuffer buffer)
        {
            return new TargetDetection(buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(),
                buffer.getDouble(), (int)buffer.getLong());
        }
        @Override
        public void pack(ByteBuffer buffer, TargetDetection detection)
        {
            buffer.putDouble(detection.area);
            buffer.putDouble(detection.tx);
            buffer.putDouble(detection.ty);
            buffer.putDouble(detection.pitch);
            buffer.putDouble(detection.yaw);
            buffer.putDouble(detection.depth);
            buffer.putLong(detection.fiducialID);
        }
    }
    public class TargetCamera
    {
        protected final StructArraySubscriber<TargetDetection> detections;
        public TargetCamera(String name)
        {
            detections = table.getStructArrayTopic(name, new TargetDetectionStruct()).subscribe(new TargetDetection[] {});
        }
        public TargetDetection[] getDetections()
        {
            return detections.get();
        }
    }
    public class PoseSupplier
    {
        protected final StructSubscriber<Pose2d> poseSubscriber;
        public PoseSupplier(String name, Consumer<Pair<Pose2d, Double>> consumer)
        {
            poseSubscriber = table.getSubTable("poses").getStructTopic(name, new Pose2dStruct()).subscribe(new Pose2d());
            NetworkTableInstance.getDefault().addListener(poseSubscriber, EnumSet.of(Kind.kValueAll), event -> {
                var pose = poseSubscriber.getAtomic();
                consumer.accept(Pair.of(pose.value, (double)pose.timestamp / 1e9));
            });
        }
    }
    public void setGlobalPose(Pose2d pose)
    {
        globalPosePublisher.set(pose);
    }
}
